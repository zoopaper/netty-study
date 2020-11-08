package org.netty.study.serializable;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class OrderClientHandler extends ChannelHandlerAdapter {

	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		for (int i = 1; i <= 10; i++) {
			ctx.write(buildRequest(i));
		}
		ctx.flush();
	}


	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		System.out.println("Receive server response :" + msg);

	}


	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	private OrderRequest buildRequest(long orderId) {
		OrderRequest request = new OrderRequest();
		request.setOrderId(orderId);
		request.setUserName("krisjin");
		request.setAddress("朝阳门外大街泛利大厦");
		request.setMobilePhone("10000000");
		request.setProductName("黑客与画家");
		return request;
	}


	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

}
