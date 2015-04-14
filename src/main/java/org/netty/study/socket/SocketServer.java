package org.netty.study.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;

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

        Socket clientSocket = null;

        BufferedReader in = null;

        PrintWriter out = null;


        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is start.............");

            while (true) {

                clientSocket = serverSocket.accept();

                SocketChannel socketChannel = clientSocket.getChannel();


                System.out.println("connect " + clientSocket.getRemoteSocketAddress());
                //channelInfo(socketChannel);

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String data = in.readLine();

                System.out.println("receive data :" + data);

                out.println("SUCCESS了!");

                out.close();

                in.close();

                clientSocket.close();
            }

        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static void channelInfo(SocketChannel socketChannel) {
        try {
            boolean isBlocking = socketChannel.isBlocking();

            boolean finishConnection = socketChannel.finishConnect();

            boolean isConnection = socketChannel.isConnected();

            boolean isRegistry = socketChannel.isRegistered();

            System.out.println("isBlocking :" + isBlocking);
            System.out.println("finishConnection :" + finishConnection);
            System.out.println("isConnection :" + isConnection);
            System.out.println("isRegistry :" + isRegistry);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
