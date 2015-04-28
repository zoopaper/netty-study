package org.netty.study.nio.buffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/4/28
 * Time: 15:04
 */
public class BufferCharView {

    public static void main(String[] args) {

        ByteBuffer byteBuffer = ByteBuffer.allocate(7).order(ByteOrder.BIG_ENDIAN);

        CharBuffer charBuffer = byteBuffer.asCharBuffer();

        byteBuffer.put(0, (byte) 0);
        byteBuffer.put(1, (byte) 'W');
        byteBuffer.put(2, (byte) 0);
        byteBuffer.put(3, (byte) 'i');
        byteBuffer.put(4, (byte) 0);
        byteBuffer.put(5, (byte) '!');
        byteBuffer.put(6, (byte) 0);

        print(byteBuffer);
        print(charBuffer);
    }

    private static void print(Buffer buffer) {

        System.out.println("position=" + buffer.position() + ", limit=" + buffer.limit() + ", capacity=" + buffer.capacity() + ", (" + buffer.toString() + ")");
    }
}
