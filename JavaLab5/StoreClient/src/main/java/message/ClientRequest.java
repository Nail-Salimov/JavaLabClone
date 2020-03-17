package message;

public class ClientRequest {
    private String header;
    private Payload payload;
    private String verifySignature;

    public ClientRequest(String header, Payload payload, String verifySignature) {
        this.header = header;
        this.payload = payload;
        this.verifySignature = verifySignature;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String handler) {
        this.header = handler;
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
