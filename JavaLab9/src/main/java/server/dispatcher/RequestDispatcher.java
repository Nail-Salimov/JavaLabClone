package server.dispatcher;

import server.State.ClientState;
import server.dto.*;

import server.protocol.Request;
import server.services.ClientService;
import server.services.GeneralService;
import server.services.SellerService;

import java.util.LinkedList;
import java.util.List;

public class RequestDispatcher {
    private ClientService clientService;
    private SellerService sellerService;
    private GeneralService generalService;
    private List<ContainerDto> listResult;

    public RequestDispatcher(ClientService clientService, SellerService sellerService, GeneralService generalService) {
        this.clientService = clientService;
        this.sellerService = sellerService;
        this.generalService = generalService;
        listResult = new LinkedList<>();
    }

    public RequestDispatcher() {
        listResult = new LinkedList<>();
    }

    public List<ContainerDto> doDispatch(Request request, ClientState state) throws IllegalStateException {
        listResult.clear();
        String action = request.getHeader();

        if (action.equals("Enter") && (!state.isEnter())) {
            String token = request.getVerifySignature();
            Long id = request.getPayloadParameter("sub", Long.class);
            String role = request.getPayloadParameter("role", String.class);

            UserDto userDto = generalService.getUser(id);
            if (userDto != null)
                state.readData(userDto);
            ReplyPayload replyPayload = generalService.checkToken(token, id, role);

            ContainerDto container = ContainerDto.createContent("Reply", replyPayload);
            addResult(container);

            if (replyPayload.getStatus().equals("pass")) {
                state.setEnter(true);
            } else if (replyPayload.getStatus().equals("refuse")) {
                state.setEnter(false);
            } else {
                throw new IllegalStateException("not correct response");
            }

            return getResult();

        } else if ((action.equals("Login")) && (!state.isEnter())) {
            Long id = request.getPayloadParameter("sub", Long.class);
            String password = request.getPayloadParameter("password", String.class);

            ReplyPayload replyPayload = generalService.checkClient(id, password);
            ContainerDto container = ContainerDto.createContent("Reply", replyPayload);
            addResult(container);

            if (replyPayload.getStatus().equals("pass")) {
                state.setEnter(true);
                UserDto userDto = generalService.getUser(id);
                if (userDto != null)
                    state.readData(userDto);
                TokenPayload tokenPayload = generalService.createToken(id, state.getRole());
                container = ContainerDto.createContent("Reply", tokenPayload);
                addResult(container);

            } else if (replyPayload.getStatus().equals("refuse")) {
                state.setEnter(false);

            } else {
                throw new IllegalStateException("not correct response");
            }

            return getResult();

        } else if ((action.equals("Buy")) && (state.isEnter())) {
            String product = request.getPayloadParameter("product", String.class);
            Integer count = request.getPayloadParameter("count", Integer.class);

            TransactionPayload transactionPayload = clientService.buyProduct(product, count);
            ContainerDto container = ContainerDto.createContent("Transaction", transactionPayload);
            addResult(container);
            return getResult();

        } else if (action.equals("Sell") && (state.isEnter())) {
            String product = request.getPayloadParameter("product", String.class);
            Integer count = request.getPayloadParameter("count", Integer.class);

            TransactionPayload transactionPayload = sellerService.sellProduct(product, count, state.getRole());
            ContainerDto container = ContainerDto.createContent("Transaction", transactionPayload);
            addResult(container);
            return getResult();

        } else if (action.equals("Logout") && (state.isEnter())) {
            ReplyPayload replyPayload = generalService.disconnect();
            ContainerDto container = ContainerDto.createContent("Logout", replyPayload);
            addResult(container);
            return getResult();

        } else if (action.equals("GetAllProducts") && (state.isEnter())) {
            AllProductsPayload allProductsPayload = clientService.listProducts();
            ContainerDto container = ContainerDto.createContent("Products", allProductsPayload);
            addResult(container);
            return getResult();

        } else if (action.equals("Delete") && (state.isEnter())) {
            String product = request.getPayloadParameter("product", String.class);
            TransactionPayload transactionPayload = sellerService.deleteProduct(product, state.getRole());
            ContainerDto container = ContainerDto.createContent("Transaction", transactionPayload);
            addResult(container);
            return getResult();

        } else {
            throw new IllegalStateException("Wrong parameter: " + action + " or field 'enter': " + state.isEnter());
        }
    }

    private List<ContainerDto> getResult() {
        return listResult;
    }

    private void addResult(ContainerDto container) {
        listResult.add(container);
    }


}
