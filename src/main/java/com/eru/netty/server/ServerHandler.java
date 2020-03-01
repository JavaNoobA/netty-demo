package com.eru.netty.server;

import com.eru.netty.protocol.Packet;
import com.eru.netty.protocol.PacketCodeC;
import com.eru.netty.protocol.request.LoginRequestPacket;
import com.eru.netty.protocol.response.LoginResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Created by eru on 2020/3/1.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(new Date() + ":客户端开始登录.....");

        ByteBuf buffer = (ByteBuf)msg;
        Packet packet = PacketCodeC.INSTANCE.decode(buffer);

        if (packet instanceof LoginRequestPacket){
            // 登录流程
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket)packet;
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());
            if (valid(loginResponsePacket)){
                loginResponsePacket.setSuccess(true);
                System.out.println(new Date() + ":登录成功");
            }else {
                loginResponsePacket.setReason("账户密码检验失败");
                loginResponsePacket.setSuccess(false);
                System.out.println(new Date() + ":登录失败");
            }
            // 登录响应
            ByteBuf responseBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
            ctx.channel().writeAndFlush(responseBuf);
        }
    }

    private boolean valid(LoginResponsePacket loginResponsePacket){
        return true;
    }
}
