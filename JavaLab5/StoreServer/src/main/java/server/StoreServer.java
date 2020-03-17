package server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import origin.ServerContext;
import resolve.MessageResolver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class StoreServer {

    private ServerSocket server;
    private List<ClientAgent> clients;
    private ServerContext serverContext;


    public StoreServer(int port, String propertyPath) {
        try {
            clients = new CopyOnWriteArrayList<ClientAgent>();
            serverContext = new ServerContext(propertyPath);

            start(port);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void start(int port) throws IOException {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started: " + port);
            while (true) {
                Socket clientSocket = server.accept();
                clients.add(new ClientAgent(clientSocket));
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            server.close();
        }
    }

    private class ClientAgent extends Thread {

        private BufferedReader in;
        private PrintWriter out;
        private Scanner scanner;
        private Socket clientSocket;
        private ObjectMapper mapper;
        private boolean needDisconnect;

        private MessageResolver resolver;


        ClientAgent(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
            scanner = new Scanner(System.in);
            mapper = new ObjectMapper();
            resolver = new MessageResolver(serverContext.getClientService(), serverContext.getSellerService());
            this.needDisconnect = false;
            start();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String message = in.readLine();
                    if (message == null || needDisconnect) {                //при отключение от сервера вручную
                        disconnect();
                        break;
                    }

                    List<String> listCommands = resolver.handle(message);

                    for (String command : listCommands){
                        out.write(command + "\n");
                        out.flush();

                        JsonNode node = mapper.readTree(command);
                        String header = node.path("header").asText();
                        if(header.equals("Logout")){
                            needDisconnect = true;
                        }
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        private void disconnect() {                //отключение от сервера
            try {
                clients.remove(this);
                clientSocket.close();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
