import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;


public class NewServer {

    private ServerSocket server;
    private List<ClientAgent> clients;
    private BdLogical bdLogical;


    public NewServer(int port, String propertyPath) {
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
        private ObjectMapper mapper;
        private String name;
        private boolean enter = false;

        ClientAgent(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            start();
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
            scanner = new Scanner(System.in);
            mapper = new ObjectMapper();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String message = in.readLine();
                    if (message == null) {                //при отключение от сервера вручную
                        disconnect();
                        if (enter) {                     //если пользователь был авторизован, то уведомляем об этом
                            sendAll(this.name + " disconnected");
                        }
                        break;
                    }

                    JsonNode rootNode = mapper.readTree(message);
                    JsonNode idNode = rootNode.path("header");
                    String action = idNode.asText();

                    if (action.equals("Login")) {
                        JsonNode node = mapper.readTree(rootNode.path("payload").toString());
                        String password = node.path("password").asText();
                        String name = node.path("name").asText();

                        while (!checkClient(name, password)) {
                            Message outMessage = new Message("Reply");           //отказ в доступе
                            outMessage.addParameter("status", "refuse");
                            String jsonValue = mapper.writeValueAsString(outMessage);
                            out.write(jsonValue + "\n");
                            out.flush();

                            message = in.readLine();
                            rootNode = mapper.readTree(message);
                            node = mapper.readTree(rootNode.path("payload").toString());
                            password = node.path("password").asText();
                            name = node.path("name").asText();

                        }
                        enter = true;
                        this.name = name;
                        Message outMessage = new Message("Reply");               //разрешение на доступ
                        outMessage.addParameter("status", "pass");
                        String jsonValue = mapper.writeValueAsString(outMessage);
                        out.write(jsonValue + "\n");
                        out.flush();

                        sendAll(this.name + " join to chat");

                    } else if (action.equals("Message")) {                              //отправка сообщения
                        JsonNode node = mapper.readTree(rootNode.path("payload").toString());
                        String clientsMessage = node.path("message").asText();
                        sendAll(clientsMessage);

                    } else if (action.equals("Logout")) {                              //отключение от сервера
                        sendAll(this.name + " disconnected");

                        Message outMessage = new Message("Reply");              //ответ об отключении
                        outMessage.addParameter("status", "disconnect");
                        String jsonValue = mapper.writeValueAsString(outMessage);
                        out.write(jsonValue + "\n");
                        out.flush();

                        disconnect();
                        break;

                    } else if (action.equals("Command")) {                              //история сообщений
                        JsonNode node = mapper.readTree(rootNode.path("payload").toString());
                        String command = node.path("command").asText();
                       if(command.equals("get messages")){
                           Integer page = node.path("page").asInt();
                           Integer size = node.path("size").asInt();

                           MessageHistory history = bdLogical.messageHistory(page, size);
                           String jsonValue = mapper.writeValueAsString(history);
                           out.write(jsonValue + "\n");
                           out.flush();

                       }else {
                           throw new IllegalStateException("Не верный параметр command");
                       }
                    } else {
                        throw new IllegalStateException("Не верный ");
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        private boolean checkClient(String name, String password) {    //проверка имени и пароля
            System.out.println("TRY: " + name + " " + password);
            return bdLogical.checkClient(name, password);
        }

        private void sendAll(String message) {               //отправка сообщений всем пользователям
            bdLogical.addMessage(this.name, message);
            System.out.println(this.name + " : " + message);
            for (ClientAgent client : clients) {
                if (client.enter) {
                    client.send(this.name, message);
                }
            }
        }

        private void send(String name, String message) {     //отправка одному пользователю
            try {
                Message outMessage = new Message("Message");
                outMessage.addParameter("name", name);
                outMessage.addParameter("message", message);
                String jsonValue = mapper.writeValueAsString(outMessage);
                out.write(jsonValue + "\n");
                out.flush();
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(e);
            }
        }

        private void disconnect() {                //отключение от сервера
            try {
                System.out.println(this.name + " disconnected");
                for (ClientAgent client : clients) {
                    if (client.name.equals(this.name)) {
                        clients.remove(this);
                        clientSocket.close();
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
