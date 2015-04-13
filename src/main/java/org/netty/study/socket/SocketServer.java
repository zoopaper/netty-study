package org.netty.study.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/4/13
 * Time: 19:18
 */
public class SocketServer {
    private static final int port = 8080;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;


        try {
            serverSocket = new ServerSocket(port);

            Socket socket = serverSocket.accept();
            System.out.println("Server is start.............");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println("recevie data:" + line);
                if ("Byte".equals(line)) {
                    break;
                }
            }

            out.println("SUCCESS");

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
