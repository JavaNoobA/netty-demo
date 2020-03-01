package com.eru.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by eru on 2020/3/1.
 */
public class NettyClient {

    private static final int MAX_RETRY = 5;
    private static final int PORT = 8000;
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(workGroup)
                // 1. 指定线程模型
                .channel(NioSocketChannel.class)
                // 2. 添加自定义属性
                .attr(AttributeKey.newInstance("clientName"), "nettyClient")
                // 3. 设置TCP底层属性
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 4. IO处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new FirstClientHandler());
                    }
                });
        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    public static void connect(Bootstrap bootstrap, String host, final int port, int retry){

        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()){
                System.out.println("连接成功");
            }else if (retry == 0){
                System.out.println("重试次数已用完, 放弃连接");
            }else {
                int order = retry - 1;
                int delay = 1 << order;
                System.err.println(new Date() + "连接失败, 第" + order + "重连");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry -1), delay,
                        TimeUnit.MILLISECONDS);
            }
        });
    }
}
