package org.netty.study.channelhandler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * @author krisjin
 */
public class ChannelCloseOutbound extends ChannelHandlerAdapter {

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise)
			throws Exception {

		System.out.println("TCP Closing!");
		ctx.close(promise);
	}


	public static void main(String[] args){
		String hex =Integer.toHexString(100);
		System.out.println((String.valueOf(Long.MAX_VALUE).length()));
	}

}
