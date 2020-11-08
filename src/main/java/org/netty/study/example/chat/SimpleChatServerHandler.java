package org.netty.study.example.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/4/13
 * Time: 12:08
 */
public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String> {
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
        }
        channels.add(ctx.channel());
    }

    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
        }
        channels.remove(ctx.channel());
    }

    protected void messageReceived(ChannelHandlerContext ctx, String s) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            if (channel != incoming) {
                channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + s + "\n");
            } else {
                channel.writeAndFlush("[you]" + s + "\n");
            }
        }
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "在线");
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "掉线");
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

    }
}
