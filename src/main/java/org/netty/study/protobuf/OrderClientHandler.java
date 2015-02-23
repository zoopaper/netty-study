package org.netty.study.protobuf;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class OrderClientHandler extends ChannelHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		for(int i=1;i<=10;i++){
			ctx.write(buildRequest(i));
		}
		ctx.flush();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("Receive server response :\n"+msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	private OrderRequestProto.OrderRequest buildRequest(int id) {
		OrderRequestProto.OrderRequest.Builder builder = OrderRequestProto.OrderRequest.newBuilder();

		builder.setOrderId(id);
		builder.setUserName("krisjin");
		builder.setProductName("netty in action");

		List<String> address = new ArrayList<String>();
		address.add("Beijing of china");
		address.add("Shanghai of china");
		builder.addAllAddress(address);
		return builder.build();

	}
}
