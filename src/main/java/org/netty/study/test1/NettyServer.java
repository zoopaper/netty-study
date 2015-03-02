package org.netty.study.test1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

	public void bind(int port) {

		EventLoopGroup bossGroup = new NioEventLoopGroup();

		EventLoopGroup workerGroup = new NioEventLoopGroup();

		ServerBootstrap boot = new ServerBootstrap();

		try {

			boot.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);

			boot.childHandler(new ChannelInitializer<SocketChannel>() {

				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();

				}
			});

			ChannelFuture cf =boot.bind(port).sync();
			cf.channel().closeFuture().sync();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {

			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();

		}

	}

	public static void main(String[] args) {
		int port = 8000;

		if (args.length > 0) {
			port = Integer.valueOf(args[0]);
		}

		new NettyServer().bind(port);

	}
}
