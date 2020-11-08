package org.netty.study.socket.chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/4/14
 * Time: 16:50
 */
public class ChatService {

    private static final int PORT = 8080;
    private static HashMap<String, Socket> socketList = new HashMap<>();
    private ExecutorService executorService;
    private ServerSocket serverSocket;

    public ChatService() {
    }

    public void service() {
        try {
            serverSocket = new ServerSocket(PORT);
            executorService = Executors.newCachedThreadPool();
            System.out.println("server start...");
            while (true) {
                Socket client = serverSocket.accept();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String username = bufferedReader.readLine();
                System.out.println(username + " login");
                socketList.put(username, client);
                executorService.execute(new ServerThread(client, bufferedReader));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ServerThread implements Runnable {

        private Socket socket;
        private BufferedReader bufferedReader;
        private PrintWriter printWriter;
        private String message;

        public ServerThread(Socket socket, BufferedReader bufferedReader) throws IOException {
            this.socket = socket;
            this.bufferedReader = bufferedReader;
        }


        public void run() {
            try {
                while ((message = bufferedReader.readLine()) != null) {
                    String[] info = message.split("\\|");
                    if (info[2].equals("exit")) {
                        socketList.remove(socket);
                        bufferedReader.close();
                        if (printWriter != null) {
                            printWriter.close();
                        }
                        socket.close();
                        System.out.println(info[0] + " exit");
                        break;
                    } else {
                        sendMessage(info[1]);
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        private void sendMessage(String to) throws IOException {
            System.out.println(message);
            Socket client = socketList.get(to);
            if (client != null) {
                printWriter = new PrintWriter(
                        new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                printWriter.println(message);
            }
        }

    }

    public static void main(String[] args) {
        new ChatService().service();
    }
}
