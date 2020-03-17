import com.beust.jcommander.JCommander;

class ForClient {
    public static void main(String[] args) {
        Starter starter = new Starter();
        JCommander jCommander = new JCommander(starter);
        jCommander.parse(args);
        starter.start();
    }


    public static class Starter {

        public void start() {
            NewClient client = new NewClient("127.0.0.1", 6666);
        }
    }
}

class For {
    public static void main(String[] args) {
        ForClient.Starter starter = new ForClient.Starter();
        JCommander jCommander = new JCommander(starter);
        jCommander.parse(args);
        starter.start();
    }

}
