package server.dto;

public class ReplyPayload implements Payload {
    private String status;
    private String description;

    private ReplyPayload(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public static ReplyPayload getPayload(String status, String description) {
        return new ReplyPayload(status, description);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
