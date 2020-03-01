package com.eru.netty.client;

import com.eru.netty.protocol.Packet;
import com.eru.netty.protocol.PacketCodeC;
import com.eru.netty.protocol.request.LoginRequestPacket;
import com.eru.netty.protocol.response.LoginResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * Created by eru on 2020/3/1.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println(new Date() + ":客户端开始登录");

        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("eru");
        loginRequestPacket.setPassword("123456");

        // 开始编码
        ByteBuf buffer = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginRequestPacket);

        // 写数据
        ctx.channel().writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf data = (ByteBuf)msg;

        Packet packet = PacketCodeC.INSTANCE.decode(data);

        if (packet instanceof LoginResponsePacket){
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket)packet;
            if (loginResponsePacket.isSuccess()){
                System.out.println(new Date() + ":客户端登录成功");
            }else {
                System.out.println(new Date() + ":客户端登录失败, 原因:" + loginResponsePacket.getReason());
            }
    }
    }
}
