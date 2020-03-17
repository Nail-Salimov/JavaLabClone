package server.repositories;

import java.util.Map;

public interface ProductRepository {
    Map<String, Integer> listProduct();
    boolean buyProduct(String name, int count);
    void addProduct(String name, int count);
    boolean deleteProduct(String name);
}
