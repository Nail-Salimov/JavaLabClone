package DB;

import java.util.Map;

public interface ClientLogical {
    boolean buyProduct(String name, int count);
    Map<String, Integer> listProduct();
}
