package org.netty.study.decoder.fixed;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoClientHandler extends ChannelHandlerAdapter {

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		super.channelRead(ctx, msg);
	}

	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		ByteBuf out = Unpooled.copiedBuffer("Krisjin  Welcome to Beijing!".getBytes());
		ctx.writeAndFlush(out);

	}

}
