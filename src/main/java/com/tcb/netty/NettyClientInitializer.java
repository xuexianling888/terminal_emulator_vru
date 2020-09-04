package com.tcb.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import sun.nio.cs.US_ASCII;

/**
 * @program: terminal_emulator
 * @description: 初始化器
 * @author: laoXue
 * @create: 2020-05-14 14:24
 **/
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        socketChannel.pipeline().addLast("decoder", new StringDecoder(new US_ASCII()));
        socketChannel.pipeline().addLast("encoder", new StringEncoder(new US_ASCII()));
        socketChannel.pipeline().addLast(new NettyClientHandler());
    }
}
