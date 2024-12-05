package org.spring;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.stream.ChunkedFile;

import java.io.File;

class FileServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final String BASE_DIR = "D:\\";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        String fileName = msg.toString(io.netty.util.CharsetUtil.UTF_8).trim();
        File file = new File(BASE_DIR + fileName);

        if (file.exists()) {
            try {
                System.out.println("Клиент запросил файл: " + fileName);
                ctx.writeAndFlush(new ChunkedFile(file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Файл не найден: " + fileName);
            ctx.writeAndFlush(Unpooled.copiedBuffer("Файл не найден".getBytes()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}