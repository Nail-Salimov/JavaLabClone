package messages;

public class ClientRequest {
    private String handler;
    private Payload payload;
    private String verifySignature;

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public String getVerifySignature() {
        return verifySignature;
    }

    public void setVerifySignature(String verifySignature) {
        this.verifySignature = verifySignature;
    }
}
