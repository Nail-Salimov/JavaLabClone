package server.services;

import server.dto.AllProductsPayload;

public interface ProductService {
    AllProductsPayload sellProduct(String name, int count);
    AllProductsPayload listProducts();
}
