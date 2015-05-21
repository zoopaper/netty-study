package org.netty.study.socket2;

import java.io.*;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/20
 * Time: 15:33
 */
public class DelimFramer implements Framer {


    private InputStream is;
    private static final byte DELIMETER = (byte) '\n';

    public DelimFramer(InputStream is) {
        this.is = is;
    }

    public void frameMsg(byte[] message, OutputStream out) throws IOException {

        for (byte b : message) {
            if (b == DELIMETER) {
                throw new IOException("");
            }
        }
        out.write(message);
        out.write(DELIMETER);
        out.flush();
        out.close();
    }

    public byte[] nextMsg() throws IOException {
        ByteArrayOutputStream msgBuffer = new ByteArrayOutputStream();

        int nextByte;
        while ((nextByte = is.read()) != DELIMETER) {
            if (nextByte == -1) {
                if (msgBuffer.size() == 0) {
                    return null;
                } else throw new EOFException("Non-empty message without delimiter");
            }

            msgBuffer.write(nextByte);
        }

        return msgBuffer.toByteArray();

    }
}
