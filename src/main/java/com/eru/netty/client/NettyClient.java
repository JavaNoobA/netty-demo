package com.eru.netty.client;

import com.eru.netty.protocol.PacketCodeC;
import com.eru.netty.protocol.request.MessageRequestPacket;
import com.eru.netty.util.LoginUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.util.Date;
import java.util.Scanner;
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
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });
        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    public static void connect(Bootstrap bootstrap, String host, final int port, int retry){

        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()){
                // 连接成功后, 启动控制台线程
                Channel channel = ((ChannelFuture)future).channel();
                startConsoleThread(channel);
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

    private static void startConsoleThread(Channel channel) {
        new Thread(()->{
          while (!Thread.interrupted()){
              if (LoginUtil.hasLogin(channel)){
                  System.out.println("输入消息发送至服务端!");
                  Scanner scanner = new Scanner(System.in);
                  String line = scanner.nextLine();
                  MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
                  messageRequestPacket.setMessage(line);

                  ByteBuf byteBuf = PacketCodeC.INSTANCE.encode(channel.alloc(), messageRequestPacket);
                  channel.writeAndFlush(byteBuf);
              }
          }
        }).start();
    }
}
