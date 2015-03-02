package org.netty.study.nio.buffer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BufferTest {

	public static void main(String[] args) {
		ByteBuffer buf = ByteBuffer.allocate(125);
		buf.put("Hello Buffer !".getBytes());

		System.out.println("Write mode:");
		System.out.println("Position: " + buf.position() + "; Limit: " + buf.limit() + "; Capacity: " + buf.capacity());

		buf.flip();

		System.out.println("Read mode:");

		System.out.println("Position: " + buf.position() + "; Limit: " + buf.limit() + "; Capacity: " + buf.capacity());

		try {
			bufWrite();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void bufWrite() throws IOException {

		ByteBuffer buf = ByteBuffer.allocate(48);
		buf.put("hello buffer 呵呵！".getBytes());

		RandomAccessFile file = new RandomAccessFile("e:/abc.txt", "rw");

		FileChannel channel = file.getChannel();

		buf.flip();
		channel.write(buf);

		
		
		buf.clear();

		file.close();

	}

}
