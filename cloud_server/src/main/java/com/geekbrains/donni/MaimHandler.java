package com.geekbrains.donni;

import com.geekbrains.donni.common.FileMessage;
import com.geekbrains.donni.common.FileRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MaimHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FileRequest) {
            FileRequest fq = (FileRequest) msg;
            if (Files.exists(Paths.get("storage_server/" + fq.getFilename()))){
                File file = new File("storage_server/" + fq.getFilename());
                long fileLength = file.length();
                if (fileLength < 10 * 1024 * 1024) {
                    FileMessage fm = new FileMessage(Paths.get("storage_server/" + fq.getFilename()));
                    ctx.writeAndFlush(fm);
                }

            }
        } else if (msg instanceof FileMessage) {
            FileMessage fm = (FileMessage) msg;
            byte[] data = fm.getData();
            File file = new File(fm.getFilename());
            if (!Files.exists(Paths.get("storage_server/" + fm.getFilename())) || (Files.exists(Paths.get("storage_server/" + fm.getFilename())) && fm.isFirstData())) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(data);
                }
            } else {
                System.out.println("Файл существует"); // переделать!
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
