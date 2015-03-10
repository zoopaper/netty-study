package org.netty.study.nio.scattergather;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

public class ScatterAndGather {

	static String headers = "header:13byte ";
	static String bodys = "body:Scattering and Gathering example";

	public static void main(String[] args) {
		gather();
		scatter();
	}

	public static void scatter() {

		ByteBuffer header = ByteBuffer.allocate(13);
		ByteBuffer body = ByteBuffer.allocate(100);

		ByteBuffer[] bufferArray = { header, body };

		ScatteringByteChannel channel = getChannel();

		try {
			channel.read(bufferArray);
		} catch (IOException e) {
			e.printStackTrace();
		}

		header.rewind();
		body.rewind();

		String headerStr = convertBufferToString(header);
		String bodyStr = convertBufferToString(body);
		System.out.println(headerStr);
		System.out.println(bodyStr);

	}

	public static void gather() {
		ByteBuffer header = ByteBuffer.allocate(14);
		ByteBuffer body = ByteBuffer.allocate(100);

		header.put(headers.getBytes());
		body.put(bodys.getBytes());

		GatheringByteChannel channel = getChannel();

		try {
			header.flip();
			body.flip();
			channel.write(new ByteBuffer[] { header, body });
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static FileChannel getChannel() {

		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile("e:/sg.txt", "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return raf.getChannel();
	}

	public static String convertBufferToString(ByteBuffer buffer) {
		StringBuilder sb = new StringBuilder();
		while (buffer.remaining() != 0) {
			sb.append((char) buffer.get());
		}
		return sb.toString();
	}

}
