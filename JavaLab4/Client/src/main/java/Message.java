import java.util.HashMap;
import java.util.Map;

public class Message <T>{
    private String header;
    private Map<String, T> payload;

    public Message(String header) {
        this.header = header;
        this.payload = new HashMap<>();
    }

    public void addParameter(String par, T value){
        payload.put(par, value);
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Map<String, T> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, T> payload) {
        this.payload = payload;
    }
}
