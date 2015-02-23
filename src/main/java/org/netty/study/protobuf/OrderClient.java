package org.netty.study.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class OrderClient {

	public void connect(String host, int port) throws InterruptedException {

		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap boot = new Bootstrap();
			boot.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true);
			boot.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
					ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
					ch.pipeline().addLast(new ProtobufDecoder(OrderResponseProto.OrderResponse.getDefaultInstance()));
					ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
					ch.pipeline().addLast(new ProtobufEncoder());
					ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
					ch.pipeline().addLast(new OrderClientHandler());
				}

			});

			ChannelFuture cf = boot.connect(host, port).sync();
			cf.channel().closeFuture().sync();

		} finally {
			group.shutdownGracefully();
		}

	}
	
	public static void main(String[] args) throws InterruptedException {
		new OrderClient().connect("127.0.0.1",8080);
	}

}
