package org.netty.study.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/4/13
 * Time: 19:35
 */
public class SocketClient {


    public static void main(String[] args) {

        Socket socket = new Socket();

        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
        System.out.println("Client is start...........");
        try {
            socket.connect(inetSocketAddress);


            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader scanIn = new BufferedReader(new InputStreamReader(System.in));


            if ((scanIn.readLine()) != null) {

                out.println(scanIn.readLine());
            }

            String line = null;

            if ((line = in.readLine()) != null) {
                System.out.println(line);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
