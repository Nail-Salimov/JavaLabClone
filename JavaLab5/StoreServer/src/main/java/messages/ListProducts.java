package messages;

import java.util.Map;

public class ListProducts implements  Payload {
    private Map<String, Integer> map;

    public ListProducts(Map<String, Integer> map) {
        this.map = map;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }
}
