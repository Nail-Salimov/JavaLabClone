package server.repositories;

import java.awt.*;
import java.util.List;
import java.util.Map;

public interface ClientRepository extends CrudRepository<Long, String> {
    boolean buyProduct(String name, int count);
    Map<Object, Object> listProduct();
}
