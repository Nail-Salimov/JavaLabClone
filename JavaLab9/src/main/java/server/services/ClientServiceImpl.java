package server.services;

import server.dto.AllProductsPayload;
import server.dto.TransactionPayload;
import server.repositories.ClientRepository;

import java.util.Map;

public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientServiceImpl(){};

    @Override
    public TransactionPayload buyProduct(String name, int count) {
        if (clientRepository.buyProduct(name, count)) {
            return TransactionPayload.createPayload("success", "successful purchase");

        } else {
            return TransactionPayload.createPayload("failure", "quantity exceeded");
        }
    }

    @Override
    public AllProductsPayload listProducts() {
        Map<Object, Object> map = clientRepository.listProduct();
        return AllProductsPayload.createPayload(map);
    }

}
