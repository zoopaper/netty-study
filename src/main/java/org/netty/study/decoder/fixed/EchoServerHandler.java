package org.netty.study.decoder.fixed;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends ChannelHandlerAdapter {


	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}


	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		System.out.println("Receive client : [" + msg + "]");

	}

}
