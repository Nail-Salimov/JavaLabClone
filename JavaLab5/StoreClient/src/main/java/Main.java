import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import server.StoreClient;


public class Main {
    public static void main(String[] args) {
        Starter starter = new Starter();
        JCommander jCommander = new JCommander(starter);
        jCommander.parse(args);
        starter.start();
    }

    private static class Starter{
        @Parameter(names = "--server-ip")
        private String ip ;

        @Parameter(names = "--server-port")
        private int port;

        public void  start() {
            StoreClient client = new StoreClient(ip, port);
        }
    }
}
