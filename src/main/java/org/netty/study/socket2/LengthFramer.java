package org.netty.study.socket2;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/20
 * Time: 16:05
 */
public class LengthFramer implements Framer {

    public static final int MAX_MESSAGE_LENGTH = 65535;
    public static final int BYTE_MASK = 0xff;
    public static final int SHORT_MASK = 0xffff;
    public static final int BYTE_SHIFT = 8;

    private DataInputStream dis;

    public LengthFramer(InputStream is) {
        this.dis = new DataInputStream(is);
    }

    @Override
    public void frameMsg(byte[] message, OutputStream out) throws IOException {

        if (message.length > MAX_MESSAGE_LENGTH) {
            throw new IOException("message too long");
        }

    }

    @Override
    public byte[] nextMsg() throws IOException {
        return new byte[0];
    }
}
