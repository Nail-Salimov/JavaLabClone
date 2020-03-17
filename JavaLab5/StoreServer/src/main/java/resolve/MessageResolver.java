package resolve;

import Service.ClientService;
import Service.SellerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jwt.Tokenizer;
import messages.ListProducts;
import messages.Message;
import messages.Payload;
import messages.Reply;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MessageResolver {
    private ClientService clientService;
    private SellerService sellerService;
    private ObjectMapper mapper;
    private boolean enter;
    private List<String> listResult;

    public MessageResolver(ClientService clientService, SellerService sellerService) {
        this.clientService = clientService;
        this.sellerService = sellerService;
        this.mapper = new ObjectMapper();
        this.enter = false;
        listResult = new LinkedList<>();
    }

    public List<String> handle(String message) throws IOException {
        listResult.clear();

        JsonNode rootNode = mapper.readTree(message);
        JsonNode idNode = rootNode.path("header");
        String action = idNode.asText();

        if ((action.equals("Enter")) && (!enter)) {
            idNode = rootNode.path("verifySignature");
            action = idNode.asText();

            JsonNode node = mapper.readTree(rootNode.path("payload").toString());
            int id = node.path("sub").asInt();
            String role = node.path("role").asText();
            enter = Tokenizer.checkClient(action, id, role);

            if (enter) {
                Payload payload = new Reply("pass", "right token");
                Message outMessage = new Message("Reply", payload);

                String jsonValue = mapper.writeValueAsString(outMessage);
                addResult(jsonValue);
                return getResult();

            } else {
                Payload payload = new Reply("refuse", "bad token");
                Message outMessage = new Message("Reply", payload);

                String jsonValue = mapper.writeValueAsString(outMessage);
                addResult(jsonValue);
                return getResult();
            }

        } else if ((action.equals("Login")) && (!enter)) {
            JsonNode node = mapper.readTree(rootNode.path("payload").toString());
            int id = node.path("sub").asInt();
            String password = node.path("password").asText();

            enter = clientService.checkClient(id, password);

            if (enter) {
                Payload payload = new Reply("pass", clientService.getRole(id));
                Message outMessage = new Message("Reply", payload);

                String jsonValue = mapper.writeValueAsString(outMessage);
                addResult(jsonValue);

                String role = clientService.getRole(id);
                payload = new Reply("New Token", Tokenizer.getToken(id, role));
                outMessage = new Message("Reply", payload);

                jsonValue = mapper.writeValueAsString(outMessage);
                addResult(jsonValue);
                return getResult();

            } else {
                Payload payload = new Reply("refuse", "wrong data");
                Message outMessage = new Message("Reply", payload);

                String jsonValue = mapper.writeValueAsString(outMessage);
                addResult(jsonValue);
                return getResult();
            }

        } else if (action.equals("Buy") && (enter)) {
            JsonNode node = mapper.readTree(rootNode.path("payload").toString());

            String name = node.path("product").asText();

            int count = node.path("count").asInt();
            boolean response = clientService.buyProduct(name, count);

            if (!response) {
                Payload payload = new Reply("failure", "quantity exceeded");
                Message outMessage = new Message("Transaction", payload);

                String jsonValue = mapper.writeValueAsString(outMessage);
                addResult(jsonValue);
                return getResult();

            } else {
                Payload payload = new Reply("success", "successful purchase");
                Message outMessage = new Message("Transaction", payload);

                String jsonValue = mapper.writeValueAsString(outMessage);
                addResult(jsonValue);
                return getResult();
            }

        } else if (action.equals("Sell") && (enter)) {
            JsonNode node = mapper.readTree(rootNode.path("payload").toString());
            String role = node.path("role").asText();

            if (role.equals("buyer")) {
                Payload payload = new Reply("failure", "not enough rights");
                Message outMessage = new Message("Transaction", payload);

                String jsonValue = mapper.writeValueAsString(outMessage);
                addResult(jsonValue);
                return getResult();

            } else {
                sellerService.sellProduct(node.path("product").asText(), Integer.parseInt(node.path("count").asText()));
                Payload payload = new Reply("success", "added");
                Message outMessage = new Message("Transaction", payload);

                String jsonValue = mapper.writeValueAsString(outMessage);
                addResult(jsonValue);
                return getResult();
            }

        } else if (action.equals("Logout")) {
            Payload payload = new Reply("logout", "GoodBye");
            Message outMessage = new Message("Logout", payload);

            String jsonValue = mapper.writeValueAsString(outMessage);
            addResult(jsonValue);
            return getResult();

        } else if (action.equals("GetAllProducts") && (enter)) {
            Payload products = new ListProducts(clientService.listProducts());
            Message outMessage = new Message("Products", products);

            String jsonValue = mapper.writeValueAsString(outMessage);
            addResult(jsonValue);
            return getResult();

        } else if (action.equals("Delete") && (enter)) {
            JsonNode node = mapper.readTree(rootNode.path("payload").toString());
            String role = node.path("role").asText();
            if (role.equals("buyer")) {
                Payload payload = new Reply("failure", "not enough rights");
                Message outMessage = new Message("Transaction", payload);

                String jsonValue = mapper.writeValueAsString(outMessage);
                addResult(jsonValue);
                return getResult();

            } else {
                sellerService.deleteProduct(node.path("product").asText());
                Payload payload = new Reply("success", "deleted");
                Message outMessage = new Message("Transaction", payload);

                String jsonValue = mapper.writeValueAsString(outMessage);
                addResult(jsonValue);
                return getResult();
            }
        } else {
            throw new IllegalStateException("Wrong parameter: " + action);
        }
    }

    private List<String> getResult() {
        return listResult;
    }

    private void addResult(String result) {
        listResult.add(result);
    }
}
