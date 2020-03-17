package origin;

import DB.*;
import Service.*;


public class ServerContext {
    private ClientService clientService;
    private SellerService sellerService;

    private ClientLogical clientLogical;
    private SellerLogical sellerLogical;
    private GeneralLogical generalLogical;

    public ServerContext(String propertyPath) {
        sellerLogical = SellerDBLogical.getSellerDBLogical(propertyPath);
        clientLogical = ClientDBLogical.getClientDBLogical(propertyPath);
        generalLogical = GeneralDBLogical.getGeneralDBLogical(propertyPath);

        sellerService = new SellerServiceImpl(sellerLogical);
        clientService = new ClientServiceImpl(clientLogical, generalLogical);
    }

    public ClientService getClientService() {
        return clientService;
    }

    public SellerService getSellerService() {
        return sellerService;
    }

    public ClientLogical getClientLogical() {
        return clientLogical;
    }

    public SellerLogical getSellerLogical() {
        return sellerLogical;
    }

}
