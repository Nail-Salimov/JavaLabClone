import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import server.context.ApplicationContextReflectionBased;
import server.dispatcher.RequestDispatcher;
import server.protocol.Server;

public class Main {
    public static void main(String[] args) {
        Starter starter = new Starter();
        JCommander jCommander = new JCommander(starter);
        jCommander.parse(args);
        starter.start();
    }

    private static class Starter {

        private int port;
        private String path;

        public void start() {
            System.out.println(port + " " + path);
            ApplicationContextReflectionBased context = new ApplicationContextReflectionBased("server");
            context.addConnection("/home/nail/Progy/JavaLab/JavaLab3/db.properties");
            context.buildProject();

            RequestDispatcher dispatcher = context.getComponent("server.dispatcher.RequestDispatcher");

            Server server = new Server();
            server.setDispatcher(dispatcher);
            server.startServer(6666);

        }
    }
}