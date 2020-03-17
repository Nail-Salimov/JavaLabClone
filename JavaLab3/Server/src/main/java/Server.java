import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;


public class Server {

    private ServerSocket server;
    private List<ClientAgent> clients;

    private static Server chatServer;
    private static String propertyPath;
    private BdLogical bdLogical;


    public Server(int port, String propertyPath) {
        try {
            clients = new CopyOnWriteArrayList<ClientAgent>();
            bdLogical = BdLogical.getBdLogical(propertyPath);
            start(port);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void start(int port) throws IOException {
        try {
            server = new ServerSocket(port);
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
        private String name;
        private boolean enter = false;

        ClientAgent(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            start();
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
            scanner = new Scanner(System.in);
        }

        @Override
        public void run() {
            String name;
            String password;
            try {
                name = in.readLine();
                password = in.readLine();

                boolean pass = checkClient(name, password);
                while (!pass){
                    if(name == null) {
                        clients.remove(this);
                        clientSocket.close();
                        return;
                    }
                        out.write("Client not found" + "\n");
                        out.flush();

                        name = in.readLine();
                        password = in.readLine();
                        pass = checkClient(name, password);
                }
                this.name = name;
                System.out.println("[" + name + " join ]");
                bdLogical.addMessage(name, "[" + name + " join ]");
                enter = true;
                for (ClientAgent client : clients) {
                    if(client.enter) {
                        client.send(name, "[" + name + " join ]");
                    }
                };
                out.flush();

                while (true) {
                    String message = in.readLine();
                    if (isDisconnected(message)) {      //если message == null
                        System.out.println("[" + name + " disconnected ]");
                        bdLogical.addMessage(name, "[" + name + " disconnected ]");
                        disconnect(name);
                        break;
                    }
                    System.out.println(name + ": " + message); //вывод в консоль сервера
                    bdLogical.addMessage(name, message);
                    for (ClientAgent client : clients) {
                        if (client.enter) {
                            client.send(name, "<" + name + ">" + ": " + message);   //отправка сообщений всем пользователям
                        }
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        private boolean checkClient(String name, String password) {
            System.out.println("TRY: " + name + " " + password);
            return bdLogical.checkClient(name, password);
        }

        private void send(String name, String message) {
            out.write(message + "\n");
            out.flush();
        }

        private boolean isDisconnected(String message) {
            if ((message == null) || (message.equals("/disconnect"))) {
                return true;
            }
            return false;
        }

        private void disconnect(String name) {
            for (ClientAgent client : clients) {
                client.send(name, "[" + name + " disconnected ]");
                if (client.name.equals(name)) {
                    try {
                        clients.remove(this);
                        clientSocket.close();
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
    }
}
