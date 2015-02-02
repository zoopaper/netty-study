package org.netty.study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerTimeServer implements Runnable {

	private Selector selector;
	private ServerSocketChannel serverSocketChannel;
	private volatile boolean stop;

	public MultiplexerTimeServer(int port) {
		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.bind(new InetSocketAddress(port), 1024);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

			System.out.println("The time server is start in port :" + port);

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	public void stop() {
		stop = true;

	}

	@Override
	public void run() {

		while (!stop) {
			try {
				selector.select(1000);
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> iter = selectedKeys.iterator();

				SelectionKey key = null;

				while (iter.hasNext()) {
					key = iter.next();
					iter.remove();

				}

			} catch (Throwable t) {
				t.printStackTrace();

			}

		}

	}

	private void handleInput(SelectionKey key) throws IOException {

		if (key.isValid()) {
			if (key.isAcceptable()) {
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel sc = ssc.accept();
				sc.configureBlocking(false);

				sc.register(selector, SelectionKey.OP_READ);

			}
			if (key.isReadable()) {
				SocketChannel sc = (SocketChannel) key.channel();

				ByteBuffer readBuffer = ByteBuffer.allocate(1024);

				int readBytes = sc.read(readBuffer);

				if (readBytes > 0) {

					readBuffer.flip();
					
					byte[] bytes = new byte[readBuffer.remaining()];
					
					readBuffer.get(bytes);
					
					String body = new String(bytes, "UTF-8");
					
					System.out.println("The time server receive order :" + body);

				}

			}
		}
	}
}
