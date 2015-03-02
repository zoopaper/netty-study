package org.netty.study.test1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

	public void connect(int port, String host, int connCounts) {

		EventLoopGroup group = new NioEventLoopGroup();

		Bootstrap boot = new Bootstrap();

		try {

			boot.group(group).channel(NioSocketChannel.class);
			boot.handler(new ChannelInitializer<SocketChannel>() {

				protected void initChannel(SocketChannel ch) throws Exception {

				}

			});

			for (int i = 0; i <= connCounts; i++) {

				boot.connect(host, port);
			}

		} finally {
			group.shutdownGracefully();
		}

	}

	public static void main(String[] args) {
		String host = "127.0.0.1";
		int connCounts = 60000;
		int port = 8000;
		if (args.length > 0) {
			port = Integer.valueOf(args[0]);
			host = args[1];
			connCounts = Integer.valueOf(args[2]);
		}

		new NettyClient().connect(port, host, connCounts);

	}

}
