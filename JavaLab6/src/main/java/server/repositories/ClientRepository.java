package server.repositories;

import java.util.Map;

public interface ClientRepository extends CrudRepository<Long, String> {
    boolean buyProduct(String name, int count);
    Map<String, Integer> listProduct();
}
