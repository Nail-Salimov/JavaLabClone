package server.services;

import server.dto.TransactionPayload;
import server.repositories.SellerRepository;

public class SellerServiceImpl implements SellerService {

    private SellerRepository sellerRepository;

    public SellerServiceImpl(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public SellerServiceImpl() {
    }

    @Override
    public TransactionPayload sellProduct(String name, int count, String role) {
        if (role.equals("admin")) {
            sellerRepository.addProduct(name, count);
            return TransactionPayload.createPayload("success", "added");
        } else {
            return TransactionPayload.createPayload("failure", "not enough rights");
        }
    }

    @Override
    public TransactionPayload deleteProduct(String name, String role) {
        if (role.equals("buyer")) {
            if (sellerRepository.deleteProduct(name)) {
                return TransactionPayload.createPayload("success", "deleted");

            } else {
                return TransactionPayload.createPayload("failure", "product does not exist");
            }

        } else {
            return TransactionPayload.createPayload("failure", "not enough rights");
        }
    }

}
