import com.beust.jcommander.JCommander;

import java.io.*;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {

        //Scanner scanner = new Scanner(System.in); //--mode multi-thread --count 3 --files https://cdn.humoraf.ru/wp-content/uploads/2017/08/23-14.jpg;https://klike.net/uploads/posts/2019-03/1551516106_1.jpg;https://klike.net/uploads/posts/2019-03/medium/1551512888_2.jpg --folder /home/nail/Изображения/
        //String bf = scanner.nextLine();
        //System.out.println(bf);
        //String[] split = bf.split(" ");
        Starter starter = new Starter();
        JCommander jCommander = new JCommander(starter);
        jCommander.parse(args);
        System.out.println("mode: " + starter.getMode() + " count: " + starter.getCount());
        System.out.println("path " + starter.getPath());
        starter.start();

    }
}
