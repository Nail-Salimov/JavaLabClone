package server.dto;

public class TokenPayload implements Payload {
    private String status;
    private String description;

    private TokenPayload(String token) {
        this.status = "New Token";
        this.description = token;
    }

    public static TokenPayload getPayload(String token){
        return new TokenPayload(token);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
