package org.netty.study.nio.buffer;

import java.nio.ByteBuffer;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/4
 * Time: 19:13
 */
public class BufferWarp {


    public static void main(String[] args) {

        byte[] len = new byte[10];

        ByteBuffer byteBuffer = ByteBuffer.wrap(len);
        byteBuffer.put((byte)1);
        byteBuffer.put((byte)1);
        byteBuffer.flip();
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(len.length);


    }
}
