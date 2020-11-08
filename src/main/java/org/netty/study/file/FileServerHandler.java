package org.netty.study.file;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.RandomAccessFile;

public class FileServerHandler extends SimpleChannelInboundHandler<String> {

    private static final String CR = System.getProperty("line.separator");


    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {

        File file = new File(msg);
        if (file.exists()) {
            if (!file.isFile()) {
                ctx.writeAndFlush("not a file :" + file + CR);
                return;
            }
            ctx.write(file + " " + file.length() + CR);

            RandomAccessFile raf = new RandomAccessFile(file, "r");
            FileRegion fileRegion = new DefaultFileRegion(raf.getChannel(), 0, raf.length());

            ctx.write(fileRegion);
            ctx.writeAndFlush(CR);
            raf.close();

        } else {
            ctx.writeAndFlush("file not found: " + file + CR);
        }
    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

    }
}
