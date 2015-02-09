package org.netty.study.started.stick;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author krisjin
 * 
 */
public class TimeClient {
	public void connect(int port, String host) {

		// 配置客户端NIO线程组
		EventLoopGroup group = new NioEventLoopGroup();

		Bootstrap bootstrap = new Bootstrap();

		try {
			bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {

						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
							ch.pipeline().addLast(new StringDecoder());
							ch.pipeline().addLast(new TimeClientHandler());
						}

					});
			// 发起异步连接操作
			ChannelFuture f = bootstrap.connect(host, port).sync();
			// 等待客户端链路关闭
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {

			group.shutdownGracefully();
		}

	}

	public static void main(String[] args) {
		int port = 8888;
		try {
			if (args.length > 0) {
				port = Integer.valueOf(args[0]);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		new TimeClient().connect(port, "127.0.0.1");
	}
}
