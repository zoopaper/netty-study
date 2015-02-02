package org.netty.study.started;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {

	public void bind(int port) throws Exception {

		EventLoopGroup bossGroup = new NioEventLoopGroup();

		EventLoopGroup workderGroup = new NioEventLoopGroup();

		ServerBootstrap b = new ServerBootstrap();
		try {
			b.group(bossGroup, workderGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChildChannelHandler());

			// 绑定端口，同步等待成功
			ChannelFuture f = b.bind(port).sync();

			// 等待服务器端端口关闭
			f.channel().closeFuture().sync();

		} finally {
			bossGroup.shutdownGracefully();
			workderGroup.shutdownGracefully();
		}

	}

	private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

		protected void initChannel(SocketChannel channel) throws Exception {
			channel.pipeline().addLast(new TimeServerHandler());
		}

	}

	public static void main(String[] args) throws Exception {
		int port = 8888;

		if (args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);

			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		new TimeServer().bind(port);
	}
}
