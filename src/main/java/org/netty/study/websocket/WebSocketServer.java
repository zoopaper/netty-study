package org.netty.study.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author krisjin
 */
public class WebSocketServer {

	public void run(int port) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();

		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap boot = new ServerBootstrap();
			boot.group(bossGroup, workerGroup);
			boot.channel(NioServerSocketChannel.class);
			boot.childHandler(new ChannelInitializer<SocketChannel>() {

				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast("http-codec", new HttpServerCodec());
					pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
					pipeline.addLast("http-chunked", new ChunkedWriteHandler());
					pipeline.addLast("handler", new WebSocketServerHandler());
				}

			});
			Channel ch = boot.bind(port).sync().channel();
			System.out.println("Web socket server started at port " + port + '.');
			System.out.println("Open your browser to http://localhost:" + port + '/');
			ch.closeFuture().sync();
			
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}

	public static void main(String[] args) throws InterruptedException {
		
		int port = 8080;
		if (args.length > 0) {
		    try {
			port = Integer.parseInt(args[0]);
		    } catch (NumberFormatException e) {
			e.printStackTrace();
		    }
		}
		new WebSocketServer().run(port);

	}
}
