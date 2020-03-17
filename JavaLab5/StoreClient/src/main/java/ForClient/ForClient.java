package ForClient;

import server.StoreClient;

public class ForClient {
    public static void main(String[] args) {
        StoreClient client = new StoreClient("127.0.0.1", 6666);
    }
}
