package org.netty.study.example.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/4/13
 * Time: 14:33
 */
public class SimpleChatClientHandler extends SimpleChannelInboundHandler<String> {

    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("client received msg:[" + s + "]");
    }
}
