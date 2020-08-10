package com.geekbrains.donni;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Network {

    private static Network instance;
    private SocketChannel channel;

    private Network() {
        Thread thread = new Thread(() -> {
            EventLoopGroup worker = new NioEventLoopGroup();
            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(worker)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel;
                                socketChannel.pipeline().addLast(
                                        new StringDecoder(),
                                        new StringEncoder(),
                                        new CallbackHandler(callBack)
                                );
                            }
                        });
                ChannelFuture future = bootstrap.connect("localhost", 8189).sync();
                future.channel().closeFuture().sync(); //blocking
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                worker.shutdownGracefully();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void sendMessage(Object message) {
        channel.writeAndFlush(message);
    }

    private Callback1 callBack;

    public void setCallBack(Callback1 callBack) {
        this.callBack = callBack;
    }

    public static Network getInstance() {
        return instance == null ? new Network() : instance;
    }
}
