public class ForServer {
    public static void main(String[] args){
        Starter starter = new Starter();
        starter.start(6666, "/home/nail/Progy/JavaLab/JavaLab3/db.properties");
    }

    private static class Starter{

        public void  start(int port, String path) {
            NewServer server = new NewServer(port, path);
        }
    }
}
