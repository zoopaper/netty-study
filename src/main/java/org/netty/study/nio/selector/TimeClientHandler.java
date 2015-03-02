package org.netty.study.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TimeClientHandler implements Runnable {
	private String host;
	private int port;
	private Selector selector;
	private SocketChannel socketChannel;
	private volatile boolean stop;

	public TimeClientHandler(int port, String host) {
		this.host = host == null ? "127.0.0.1" : host;
		this.port = port;

		try {
			selector = selector.open();
			socketChannel = socketChannel.open();
			socketChannel.configureBlocking(false);

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void run() {
		try {
			doConnect();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		while (!stop) {
			try {
				selector.select(1000);
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> iter = selectedKeys.iterator();

				SelectionKey key = null;

				while (iter.hasNext()) {

					key = iter.next();
					iter.remove();
					try {
						handleInput(key);
					} catch (Exception e) {
						if (key != null) {
							key.cancel();
							if (key.channel() != null) {
								key.channel().close();
							}
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);

			}

		}
		System.out.println("client request is end...");

	}

	private void doConnect() throws IOException {
		if (socketChannel.connect(new InetSocketAddress(host, port))) {
			socketChannel.register(selector, SelectionKey.OP_READ);
			doWrite(socketChannel);
		} else {
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		}

	}

	private void doWrite(SocketChannel sc) throws IOException {

		byte[] req = "QUERY TIME ORDER".getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
		writeBuffer.put(req);
		writeBuffer.flip();
		sc.write(writeBuffer);

		if (!writeBuffer.hasRemaining()) {
			System.out.println("Send order 2 server succesed.");

		}
	}

	private void handleInput(SelectionKey key) throws IOException {

		if (key.isValid()) {
			SocketChannel sc = (SocketChannel) key.channel();
			if (key.isConnectable()) {
				if (sc.finishConnect()) {
					sc.register(selector, SelectionKey.OP_READ);
					doWrite(sc);
				} else {
					System.exit(1);
				}

			}

			if (key.isReadable()) {
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readBuffer);
				if (readBytes > 0) {
					readBuffer.flip();
					byte[] bytes = new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String body = new String(bytes, "UTF-8");

					System.out.println("Now is :" + body);
					this.stop = true;
				} else if (readBytes < 0) {
					// 对端链路关闭
					key.cancel();
					sc.close();
				} else {

				}
			}

		}
	}

}
