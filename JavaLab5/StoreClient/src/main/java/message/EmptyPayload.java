package message;

public class EmptyPayload implements Payload {
    private String nothing = "nothing";

    public String getNothing() {
        return nothing;
    }

    public void setNothing(String nothing) {
        this.nothing = nothing;
    }
}
