package org.spring;

import io.netty.channel.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

class FileClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final String OUTPUT_DIR = "D:\\test\\";

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имя файла для запроса:");
        String fileName = scanner.nextLine();
        ctx.writeAndFlush(Unpooled.copiedBuffer(fileName.getBytes()));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        try {
            byte[] bytes = new byte[msg.readableBytes()];
            msg.readBytes(bytes);
            Files.write(Paths.get(OUTPUT_DIR + "received_file"), bytes);
            System.out.println("Файл получен: " + OUTPUT_DIR + "received_file");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
