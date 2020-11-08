package org.netty.study.protobuf;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author krisjin
 */
public class OrderServerHandler extends ChannelHandlerAdapter {


    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        OrderRequestProto.OrderRequest req = (OrderRequestProto.OrderRequest) msg;

        if ("krisjin".equals(req.getUserName())) {
            System.out.println("Order Service accept client request:\n" + req.toString());
            ctx.writeAndFlush(buildResponse(req.getOrderId()));
        }

    }

    private OrderResponseProto.OrderResponse buildResponse(int id) {

        OrderResponseProto.OrderResponse.Builder builder = OrderResponseProto.OrderResponse.newBuilder();

        builder.setOrderId(id);
        builder.setStatusCode(0);
        builder.setDesc("Success accept order ,and send to address 好");
        return builder.build();

    }



    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
