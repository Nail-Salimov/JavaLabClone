package server;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import message.*;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoreClient {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;
    private ObjectMapper mapper;

    private BufferedReader br;
    private BufferedWriter bw;

    private int sub;
    private String role;
    private String password;
    private String token = "";

    private final String DISCONNECT = "/disconnect";
    private final String INFO = "/info";
    private final String HELP = "/help";
    private final String COMMON_COMMAND = "/";
    private final String BUY = "/buy";
    private final String SELL = "/sell";
    private final String ALL_PRODUCTS = "/all";
    private final String DELETE = "/delete";

    private final String EMPTY_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZm1jaDA4MzJ5YyIsImlkIjotMTAsInJvbGUiOiJhZGYifQ.pRj2QwxtDV55ruetyhO90eKq0KparUUCg0TLAiy8_y4";

    public StoreClient(String ip, int port) {
        start(ip, port);
    }

    public void start(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            scanner = new Scanner(System.in);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
            mapper = new ObjectMapper();

            bw = new BufferedWriter(new FileWriter("token.txt", true));
            br = new BufferedReader(new FileReader("token.txt"));

            String id = br.readLine();
            sub = Integer.parseInt((id == null) ? "-1" : id);
            role = br.readLine();
            token = br.readLine();

            token = token == null ? EMPTY_TOKEN : token;

            boolean connect = false;

            Payload payload = new ClientToken(sub, role);                                   //отправка токена с данными
            ClientRequest request = new ClientRequest("Enter", payload, token);
            String jsonValue = mapper.writeValueAsString(request);
            out.write(jsonValue + "\n");
            out.flush();

            String jsonReply = in.readLine();                                              //ответ на токен
            JsonNode node = mapper.readTree(jsonReply);
            String action = node.path("header").asText();


            if (action.equals("Reply")) {
                node = mapper.readTree(node.path("payload").toString());
                String status = node.path("status").asText();

                if (status.equals("pass")) {
                    connect = true;
                    System.out.println("Welcome to store!");
                } else if (status.equals("refuse")) {
                    if (!token.equals(EMPTY_TOKEN)) {
                        System.out.println("Error: " + node.path("description").asText());
                    }
                } else {
                    throw new IllegalStateException("not correct parameter 'Reply'");
                }
            } else {
                throw new IllegalStateException("not correct response");
            }

            while (!connect) {
                authorization();

                String jsonMessage = in.readLine();
                JsonNode rootNode = mapper.readTree(jsonMessage);
                JsonNode idNode = rootNode.path("header");
                action = idNode.asText();
                if (!action.equals("Reply")) {
                    throw new IllegalStateException("не верный ответ");
                }

                node = mapper.readTree(rootNode.path("payload").toString());
                String answer = node.path("status").asText();
                if (answer.equals("pass")) {                                            //новый токен
                    role = (node.path("description").asText());
                    connect = true;

                    jsonMessage = in.readLine();
                    rootNode = mapper.readTree(jsonMessage);
                    idNode = rootNode.path("header");
                    action = idNode.asText();

                    if (!action.equals("Reply")) {
                        throw new IllegalStateException("Ожидался Reply, а получили [" + action + "]");
                    }
                    node = mapper.readTree(rootNode.path("payload").toString());
                    if (!node.path("status").asText().equals("New Token")) {
                        throw new IllegalStateException("Ожидался New Token, а получили [" + node.path("status").asText() + "]");
                    }
                    token = node.path("description").asText();
                    bw.write(sub + "\n");
                    bw.flush();
                    bw.write(role + "\n");
                    bw.flush();
                    bw.write(token + "\n");
                    bw.close();
                    System.out.println("Welcome to store!");

                } else if (answer.equals("refuse")) {                                   //отказан в доступе
                    System.out.println("Error: " + node.path("description").asText());

                } else {
                    throw new IllegalStateException("[" + answer + "]" + " not correct parameter 'Reply'");
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
        System.out.print("Id: ");
        sub = Integer.parseInt(scanner.nextLine());
        System.out.println(" ");
        System.out.print("Password: ");
        password = scanner.nextLine();
        try {
            Payload payload = new ClientData(sub, password);
            ClientRequest request = new ClientRequest("Login", payload, token);
            String jsonValue = mapper.writeValueAsString(request);
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

                    if (action.equals("Transaction")) {
                        JsonNode node = mapper.readTree(rootNode.path("payload").toString());
                        String status = node.path("status").asText();

                        if (status.equals("success")) {
                            System.out.println(node.path("description").asText());

                        } else if (status.equals("failure")) {
                            System.out.println(node.path("description").asText());

                        } else {
                            throw new IllegalStateException("not correct parameter 'Transaction'");
                        }

                    } else if (action.equals("Logout")) {
                        disconnect();
                        System.out.println(mapper.readTree(rootNode.path("payload").toString())
                                .path("description").asText());
                        break;

                    } else if (action.equals("Products")) {
                        JsonNode node = mapper.readTree(rootNode.path("payload").toString());
                        node = node.path("map");

                        TypeFactory factory = TypeFactory.defaultInstance();
                        MapType type = factory.constructMapType(HashMap.class, String.class, Integer.class);
                        Map<String, Integer> result = mapper.readValue(node.toString(), type);

                        for (Map.Entry<String, Integer> entry : result.entrySet()) {
                            System.out.println(entry.getKey() + " : " + entry.getValue());
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
                    String[] split = word.split(" ");

                    if (word.charAt(0) == '/') {
                        if (split[0].equals(HELP)) {
                            System.out.println(
                                    "help: /help\n" +
                                            "disconnect: /disconnect\n" +
                                            "buy: /buy 'product' 'count'\n" +
                                            "sell: /sell 'product' 'count'\n"
                            );

                        } else if (split[0].equals(DISCONNECT)) {
                            Payload payload = new EmptyPayload();
                            ClientRequest request = new ClientRequest("Logout", payload, token);
                            String jsonValue = mapper.writeValueAsString(request);
                            out.write(jsonValue + "\n");
                            out.flush();
                            stop = true;

                        } else if (split[0].equals(BUY)) {
                            Pattern pattern = Pattern.compile("/buy\\s(.+)\\s(\\d+)");
                            Matcher matcher = pattern.matcher(word);

                            if(matcher.find()) {
                                String product = split[1];
                                int count = Integer.parseInt(split[2]);

                                Payload payload = new TransactionPayload(product, count, role);
                                ClientRequest request = new ClientRequest("Buy", payload, token);
                                String jsonValue = mapper.writeValueAsString(request);
                                out.write(jsonValue + "\n");
                                out.flush();
                            } else {
                                System.out.println("not correct parameters /buy");
                            }

                        } else if (split[0].equals(SELL)) {
                            Pattern pattern = Pattern.compile("/sell\\s(.+)\\s(\\d+)");
                            Matcher matcher = pattern.matcher(word);

                            if(matcher.find()) {
                                String product = split[1];
                                int count = Integer.parseInt(split[2]);

                                Payload payload = new TransactionPayload(product, count, role);
                                ClientRequest request = new ClientRequest("Sell", payload, token);
                                String jsonValue = mapper.writeValueAsString(request);
                                out.write(jsonValue + "\n");
                                out.flush();
                            }else {
                                System.out.println("not correct parameters /sell");
                            }

                        } else if (split[0].equals(ALL_PRODUCTS)) {
                            Payload payload = new EmptyPayload();
                            ClientRequest request = new ClientRequest("GetAllProducts", payload, token);
                            String jsonValue = mapper.writeValueAsString(request);
                            out.write(jsonValue + "\n");
                            out.flush();

                        }else if (split[0].equals(DELETE)){
                            Pattern pattern = Pattern.compile("/delete\\s(.+)");
                            Matcher matcher = pattern.matcher(word);

                            if(matcher.find()) {
                                String product = split[1];

                                Payload payload = new TransactionPayload(product, 0, role);
                                ClientRequest request = new ClientRequest("Delete", payload, token);
                                String jsonValue = mapper.writeValueAsString(request);
                                out.write(jsonValue + "\n");
                                out.flush();
                            }else {
                                System.out.println("not correct parameters /delete");
                            }
                        } else {
                            System.out.println("not correct command");
                        }
                    } else {
                        System.out.println("it's not command");
                    }

                }
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(e);
            }
        }
    }

}
