package server.dto;

public class TransactionPayload implements  Payload{
    private String status;
    private String description;

    private TransactionPayload(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public static TransactionPayload createPayload(String status, String description){
        return new TransactionPayload(status, description);
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
