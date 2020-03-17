public class Node {
    private String name;
    private String message;

    public Node(String name, String message) {
        this.name = name;
        this.message = message;
    }
    public Node(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}