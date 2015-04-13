package org.netty.study.example;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/4/13
 * Time: 14:33
 */
public class SimpleChatClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {

        System.out.println("client received msg:[" + msg + "]");

    }
}
