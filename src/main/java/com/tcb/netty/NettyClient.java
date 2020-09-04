package com.tcb.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @program: terminal_emulator
 * @description: 客户端
 * @author: laoXue
 * @create: 2020-05-14 14:18
 **/
public final class NettyClient {
    private static Bootstrap bootstrap;

    /**
     * 初始化Bootstrap
     */
    static {
        bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup()).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new NettyClientInitializer());
    }

    private NettyClient() {
    }

    public static Bootstrap getBootstrap() {
        return bootstrap;
    }

}
