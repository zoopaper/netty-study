package org.netty.study.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/4/13
 * Time: 19:35
 */
public class SocketClient {


    public static void main(String[] args) {

        Socket socket = null;

        BufferedReader in = null;

        PrintWriter out = null;


        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
        System.out.println("Client is start...........");
        try {
            //创建Socket套接字
            socket = new Socket();
            //与服务端建立连接
            socket.connect(inetSocketAddress);

            System.out.println("connect...........");

//            SocketChannel socketChannel = socket.getChannel();
//            channelInfo(socketChannel);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //成功建立连接后，得到一个输出流，向服务端发送数据
            out = new PrintWriter(socket.getOutputStream());


//            BufferedReader line = new BufferedReader(new InputStreamReader(System.in));

            out.println("Hello Server!");
            out.flush();


            out.close();
            in.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
