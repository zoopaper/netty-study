package org.netty.study.serializable;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class OrderServerHandler extends ChannelHandlerAdapter {


	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		OrderRequest request = (OrderRequest) msg;

		if ("krisjin".equals(request.getUserName())) {

			System.out.println("Service accept client order request: [" + request.toString() + "]");
			ctx.writeAndFlush(getResponse(request.getOrderId()));
		}

	}

	private OrderResponse getResponse(long orderId) {
		OrderResponse resp = new OrderResponse();

		resp.setOrderId(orderId);
		resp.setRespCode(0);
		resp.setDesc("Receive order success,we are as quickly as possible send...");
		return resp;
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
