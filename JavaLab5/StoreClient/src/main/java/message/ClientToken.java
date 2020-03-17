package message;

public class ClientToken implements Payload {
    private int sub;
    private String role;

    public ClientToken(int sub, String role) {
        this.sub = sub;
        this.role = role;
    }

    public int getSub() {
        return sub;
    }

    public void setId(int sub) {
        this.sub = sub;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
