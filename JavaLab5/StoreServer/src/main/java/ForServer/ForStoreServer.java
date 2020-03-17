package ForServer;

import Service.UserService;
import server.StoreServer;

public class ForStoreServer {
    public static void main(String[] args) {
        StoreServer server = new StoreServer(6666, "/home/nail/Progy/JavaLab/JavaLab3/db.properties");
    }
}
