public class MessageHistory {
    private String header;
    private String state;
    private Node[] data;
    private int count;

    public MessageHistory(String header, String state, int size) {
        this.header = header;
        this.state = state;
        count = 0;
        data = new Node[size];
    }

    public void add(String name, String text) {
        if (count == data.length) {
            throw new IllegalStateException("переполнение списка сообщений");
        }
        data[count] = new Node(name, text);
        count++;

    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHeader() {
        return header;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Node[] getData() {
        return data;
    }

    public void setData(Node[] data) {
        this.data = data;
    }



}
