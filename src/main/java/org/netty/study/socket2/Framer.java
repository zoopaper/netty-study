package org.netty.study.socket2;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/20
 * Time: 16:03
 */
public interface Framer {

    public void frameMsg(byte[] message, OutputStream out) throws IOException;

    public byte[] nextMsg() throws IOException;
}
