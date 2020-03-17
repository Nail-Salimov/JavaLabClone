import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Main {
    public static void main(String[] args){
        Starter starter = new Starter();
        JCommander jCommander = new JCommander(starter);
        jCommander.parse(args);
        starter.start();
    }

    private static class Starter{
        @Parameter(names = "--port")
        private int port;

        @Parameter(names = "--db-properties")
        private String path;

        public void  start() {
            System.out.println(port + " " + path);
            NewServer server = new NewServer(port, path);
        }
    }
}
