package message;

public class TransactionPayload implements Payload {
    private String product;
    private Integer count;
    private String role;

    public TransactionPayload(String product, int count, String role) {
        this.product = product;
        this.count = count;
        this.role = role;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
