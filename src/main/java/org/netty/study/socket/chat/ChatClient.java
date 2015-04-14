package org.netty.study.socket.chat;

import java.io.*;
import java.net.Socket;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/4/14
 * Time: 17:00
 */
public class ChatClient {


    private static final String HOST = "localhost";
    private static final int PORT = 8080;


    BufferedReader bufferedReader;
    PrintWriter printWriter;
    String strMessage;

    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        String strMessage;


        try {
            socket = new Socket(HOST, PORT);

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            BufferedReader line = new BufferedReader(new InputStreamReader(System.in));
            new ResponseHandle(bufferedReader).start();


            while (true) {
                String str = line.readLine();
                printWriter.println(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            printWriter.close();

            try {
                bufferedReader.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    static class ResponseHandle extends Thread {
        BufferedReader reader;

        ResponseHandle(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public void run() {
            while (true) {

                try {
                    String line = reader.readLine();
                    if (reader != null && !line.equals("") && null != line) {
                        String[] msg = line.split("\\|");
                        String sendMsg = msg[0] + ":" + msg[2];
                        System.out.println(sendMsg);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
