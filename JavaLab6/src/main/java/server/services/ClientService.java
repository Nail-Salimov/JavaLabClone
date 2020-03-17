package server.services;

import server.dto.AllProductsPayload;
import server.dto.TransactionPayload;

public interface ClientService {
    TransactionPayload buyProduct(String name, int count);
    AllProductsPayload listProducts();
}
