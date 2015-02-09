package org.netty.study.started.stick;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author krisjin
 * 
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

	private int counter;

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		String body = (String) msg;

		System.out.println("The time server receive order :" + body + " ; the counter is :" + ++counter);

		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(System.currentTimeMillis())
				.toString() : "BAD ORDER";

		currentTime += System.getProperty("line.separator");

		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.write(resp);

	}

	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

}
