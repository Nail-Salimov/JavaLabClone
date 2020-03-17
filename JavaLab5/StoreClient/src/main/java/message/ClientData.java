package message;

public class ClientData implements Payload {
    private int sub;
    private String password;

    public ClientData(int sub, String password) {
        this.sub = sub;
        this.password = password;
    }

    public int getSub() {
        return sub;
    }

    public void setSub(int sub) {
        this.sub = sub;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
