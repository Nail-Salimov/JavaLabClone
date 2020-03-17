package ForClient;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

class SimpleSocketClient {
    final static String STOP_WORD = "stop!";
    public static void main(String[] args){
        String url = args[0];
        int port = Integer.parseInt(args[1]);
        try{
            Socket s = new Socket(url, port);

            InputStream inStream = s.getInputStream();
            OutputStream outStream = s.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outStream));

            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); // to read console

                System.out.println("Успешно поключились к "+url+":"+port);
                System.out.println("Для отправки текста на сервер вводите строки, ответ сервера будет отображаться в этой же консоли.\n\n");
                System.out.println("Отправьте \'"+STOP_WORD+"\' для того, чтобы закрыть соединение");
                String line = "";
                String serverLine = "";
                do {
                    do {
                        serverLine = in.readLine();
                        if (serverLine!=null) System.out.println(serverLine);
                    } while (in.ready()&&(serverLine!=null));
                    if (serverLine!=null) {
                        System.out.print("#: ");
                        line = reader.readLine();
                        out.write(line + "\n");
                        out.flush();
                    }
                } while (!line.trim().equals(STOP_WORD)&&serverLine!=null);
            } finally{
                in.close();
                out.close();
                s.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            printUsageInfo();
        }
    }
    public static void printUsageInfo(){
        System.out.println("\n\nИспользоыание: "+SimpleSocketClient.class.getName()+" server_url "+" server_port_to_connect");
    }

}
