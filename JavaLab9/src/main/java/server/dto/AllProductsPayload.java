package server.dto;

import java.util.Map;

public class AllProductsPayload implements Payload {
    private Map<String, Integer> map;

    private AllProductsPayload(Map map) {
        this.map = map;
    }

    public static AllProductsPayload createPayload(Map map) {
        return new AllProductsPayload(map);
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
