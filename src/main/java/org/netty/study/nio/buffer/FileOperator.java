package org.netty.study.nio.buffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileOperator {

	public static void main(String[] args) throws IOException {
		RandomAccessFile raf = new RandomAccessFile("e:/a.txt", "rw");
		
		FileChannel channel = raf.getChannel();

		ByteBuffer buffer = ByteBuffer.allocate(1024);

		int len;

		while ((len = channel.read(buffer)) != -1) {

			System.out.println("Read " + len);

			buffer.flip();
			while (buffer.hasRemaining()) {

				System.out.print((char) buffer.get());
			}

			buffer.compact();

		}
		raf.close();
	}

}
