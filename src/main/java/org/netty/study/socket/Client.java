package org.netty.study.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/4/14
 * Time: 16:10
 */
public class Client {

    Socket socket;
    BufferedReader in;
    PrintWriter out;

    public Client() {
        try {
            socket = new Socket("localhost", 8080);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader line = new BufferedReader(new InputStreamReader(System.in));

            while (true){
                String data =in.readLine();
                System.out.println("response data: "+data);
                out.println(line.readLine());
            }
        } catch (IOException e) {
        } finally {
            try {
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        new Client();
    }
}
