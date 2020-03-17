import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewClient {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;
    private ObjectMapper mapper;


    private String name;
    private String password;

    private final String DISCONNECT = "/disconnect";
    private final String INFO = "/info";
    private final String HELP = "/help";
    private final String COMMON_COMMAND = "/";

    public NewClient(String ip, int port) {
        start(ip, port);
    }

    public void start(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            scanner = new Scanner(System.in);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
            mapper = new ObjectMapper();

            boolean connect = false;
            while (!connect) {
                authorization();

                String jsonMessage = in.readLine();
                JsonNode rootNode = mapper.readTree(jsonMessage);
                JsonNode idNode = rootNode.path("header");
                String action = idNode.asText();
                if (!action.equals("Reply")) {
                    throw new IllegalStateException("не верный ответ");
                }

                JsonNode node = mapper.readTree(rootNode.path("payload").toString());
                String answer = node.path("status").asText();
                if (answer.equals("pass")) {                                            //доступ разрешен
                    connect = true;
                    System.out.println("Подлючились к чату");
                } else if (answer.equals("refuse")) {                                   //отказан в доступе
                    System.out.println("Повторите попытку");
                } else {
                    System.out.println(answer);
                    throw new IllegalStateException("Не верный параметр ответа от сервера");
                }
            }

            new ClientReader().start();
            new ClientWriter().start();

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void authorization() {
        scanner = new Scanner(System.in);
        System.out.println("<---log in please--->");
        System.out.print("Name: ");
        name = scanner.nextLine();
        System.out.println(" ");
        System.out.print("Password: ");
        password = scanner.nextLine();
        try {
            Message message = new Message("Login");             //отправка имени и пароля
            message.addParameter("name", name);
            message.addParameter("password", password);
            String jsonValue = mapper.writeValueAsString(message);
            out.write(jsonValue + "\n");
            out.flush();
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private void disconnect() {
        try {
            clientSocket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private class ClientReader extends Thread {

        @Override
        public void run() {

            while (true) {
                try {
                    if (clientSocket.isClosed()) {
                        break;
                    }
                    String inMessage = in.readLine();
                    JsonNode rootNode = mapper.readTree(inMessage);
                    JsonNode idNode = rootNode.path("header");
                    String action = idNode.asText();

                    if (action.equals("Message")) {
                        JsonNode node = mapper.readTree(rootNode.path("payload").toString());
                        String name = mapper.readTree(node.toString()).path("name").asText();
                        String message = mapper.readTree(node.toString()).path("message").asText();
                        System.out.println("<" + name + ">" + " : " + message);

                    } else if (action.equals("Reply")) {
                        JsonNode node = mapper.readTree(rootNode.path("payload").toString());
                        String name = mapper.readTree(node.toString()).path("status").asText();
                        if (name.equals("disconnect")) {
                            disconnect();
                            break;
                        } else {
                            throw new IllegalStateException("Не верный параметр ответа");
                        }
                    } else if (action.equals("History")) {
                        String state = rootNode.path("state").asText();

                        if (state.equals("success")) {
                            System.out.println("reading");
                            String node = rootNode.path("data").toString();

                            List<Node> list = Arrays.asList(mapper.readValue(node, Node[].class));
                            for (Node n: list) {
                                System.out.println("{" + n.getName() + " : " + n.getMessage() + "}");
                            }
                        } else if (state.equals("failure")) {
                            System.out.println("Не существует столько сообщений");
                        } else {
                            throw new IllegalStateException("Не верный параметр History");
                        }
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    private class ClientWriter extends Thread {

        @Override
        public void run() {
            try {
                boolean stop = false;
                while (true) {
                    if (clientSocket.isClosed() || stop) {
                        break;
                    }
                    String word = scanner.nextLine();
                    if (word.charAt(0) == '/') {                                     // /help
                        if (word.equals(HELP)) {
                            System.out.println(
                                    "/info : info\n" +
                                            "/help : help\n" +
                                            "/disconnect : disconnect\n");
                        } else if (word.equals(DISCONNECT)) {                        // disconnect
                            Message message = new Message("Logout");

                            String jsonValue = mapper.writeValueAsString(message);
                            out.write(jsonValue + "\n");
                            out.flush();
                            stop = true;
                        } else if ((word.split(" ")[0].equals("/h")) && (word.split(" ").length == 3)) {   // /h 5 5
                            Pattern pattern = Pattern.compile("/h\\s(\\d+)\\s(\\d+)");
                            Matcher matcher = pattern.matcher(word)
                                    ;
                            if(matcher.find()) {
                                int page = Integer.parseInt(word.split(" ")[1]);
                                int size = Integer.parseInt(word.split(" ")[2]);

                                Message message = new Message("Command");
                                message.addParameter("command", "get messages");
                                message.addParameter("page", page);
                                message.addParameter("size", size);
                                String jsonValue = mapper.writeValueAsString(message);
                                out.write(jsonValue + "\n");
                                out.flush();
                            } else {
                                System.out.println("Не верные параметры /h");
                            }
                        } else {
                            System.out.println(word + " command not found");         //command not found
                        }
                    } else {
                        Message message = new Message("Message");             //отправка сообщение
                        message.addParameter("message", word);
                        String jsonValue = mapper.writeValueAsString(message);
                        out.write(jsonValue + "\n");
                        out.flush();
                    }
                }
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
