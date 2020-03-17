package Service;

import java.util.Map;

public interface ClientService {
    boolean buyProduct(String name, int count);
    Map<String, Integer> listProducts();
    boolean checkClient(int id, String password);
    String getRole(int id);
}
