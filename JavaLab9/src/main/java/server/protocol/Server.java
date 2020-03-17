package server.protocol;


import com.fasterxml.jackson.databind.ObjectMapper;
import server.State.ClientState;
import server.dispatcher.RequestDispatcher;
import server.dto.ContainerDto;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    private ServerSocket server;
    private List<ClientHandler> clients;
    private RequestDispatcher dispatcher;


    public Server() {
        clients = new CopyOnWriteArrayList<ClientHandler>();
    }

    public Server(RequestDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        clients = new CopyOnWriteArrayList<ClientHandler>();
    }

    public void startServer(int port) {
        try {
            start(port);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void start(int port) throws IOException {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started: " + port);
            while (true) {
                Socket clientSocket = server.accept();
                clients.add(new ClientHandler(clientSocket));
            }

        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            server.close();
        }
    }

    public RequestDispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(RequestDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public class ClientHandler extends Thread {

        private BufferedReader in;
        private PrintWriter out;
        private Scanner scanner;
        private Socket clientSocket;
        private ObjectMapper mapper;
        private boolean needDisconnect;
        private ClientState state;

        ClientHandler(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
            scanner = new Scanner(System.in);
            mapper = new ObjectMapper();
            this.needDisconnect = false;
            state = new ClientState();
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
                    Request request = new Request(message);
                    List<ContainerDto> listDto = dispatcher.doDispatch(request, state);
                    for (ContainerDto c : listDto) {
                        String json = mapper.writeValueAsString(c);
                        out.write(json + "\n");
                        out.flush();
                        if (c.getHeader().equals("Logout")) {
                            needDisconnect();
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

        public void needDisconnect() {
            this.needDisconnect = true;
        }
    }
}
