package server.services;

import server.dto.TransactionPayload;

public interface SellerService {
    TransactionPayload sellProduct(String name, int count, String role);
    TransactionPayload deleteProduct(String name, String role);
}
