package Service;

import DB.ClientLogical;
import DB.GeneralLogical;

import java.util.Map;

public class ClientServiceImpl implements ClientService {
    private ClientLogical clientLogical;
    private GeneralLogical generalLogical;

    public ClientServiceImpl(ClientLogical clientLogical, GeneralLogical generalLogical) {
        this.clientLogical = clientLogical;
        this.generalLogical = generalLogical;
    }

    @Override
    public boolean buyProduct(String name, int count) {
        return clientLogical.buyProduct(name, count);
    }

    @Override
    public Map<String, Integer> listProducts() {
        return clientLogical.listProduct();
    }

    @Override
    public boolean checkClient(int id, String password) {
        return generalLogical.checkClient(id, password);
    }

    @Override
    public String getRole(int id) {
        return generalLogical.getRole(id);
    }
}
