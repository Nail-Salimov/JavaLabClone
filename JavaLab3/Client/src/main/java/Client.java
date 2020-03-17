import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;


    private String name;
    private String password;

    private final String DISCONNECT = "/disconnect";
    private final String INFO = "/info";
    private final String HELP = "/help";
    private final String COMMON_COMMAND = "/";

    public Client(String ip, int port) {
        start(ip, port);
    }

    public void start(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            scanner = new Scanner(System.in);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());

            name = "no";
            password = "no";

            authorization();
            String m;
            while (!( m = in.readLine()).equals ("[" + name + " join ]")){
                System.out.println(m);
                authorization();
            }
            System.out.println(m);
            new ClientReader().start();
            new ClientWriter().start();

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void authorization() {
        scanner = new Scanner(System.in);
        System.out.println("<---log in please--->");
        System.out.print("Name: ");
        name = scanner.nextLine();
        System.out.println(" ");
        System.out.print("Password: ");
        password = scanner.nextLine();
        out.write(name + "\n");
        out.flush();
        out.write(password + "\n");
        out.flush();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    private void disconnect() {
        try {
            clientSocket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private class ClientReader extends Thread {

        @Override
        public void run() {

            while (true) {
                try {
                    if (clientSocket.isClosed()) {
                        break;
                    }
                    String inMessage = in.readLine();
                    if (inMessage != null) {
                        System.out.println(inMessage);
                    }
                    if (inMessage.equals("[" + name + " disconnected ]")) {
                        disconnect();
                        break;
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    private class ClientWriter extends Thread {

        @Override
        public void run() {
            boolean stop = false;
            while (true) {
                if (clientSocket.isClosed() || stop) {
                    break;
                }
                String word = scanner.nextLine();
                if (word.charAt(0) == '/') {
                    if (word.equals(HELP)) {
                        System.out.println(
                                "/info : info\n" +
                                        "/help : help\n" +
                                        "/disconnect : disconnect\n");
                    } else if (word.equals(DISCONNECT)) {
                        out.write("/disconnect" + "\n");
                        stop = true;
                    } else {
                        System.out.println(word + " command not found");
                    }
                } else {
                    out.write(word + "\n");
                }
                out.flush();

            }
        }


    }

}
