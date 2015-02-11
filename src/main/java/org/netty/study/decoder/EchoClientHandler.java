package org.netty.study.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoClientHandler extends ChannelHandlerAdapter {
	int counter = 0;
	static final String ECHO_REQ = "Hi, krisjin. Welcome to Netty.$_";

	public EchoClientHandler() {

	}

	public void channelActive(ChannelHandlerContext ctx) {
		for (int i = 0; i < 10; i++) {
			ByteBuf write = Unpooled.copiedBuffer(ECHO_REQ.getBytes());

			ctx.writeAndFlush(write);

		}
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		System.out.println("This is " + ++counter + "times recevie server : [" + msg + "]");

	}

	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
