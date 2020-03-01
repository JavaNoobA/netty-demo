package com.eru.netty.server;

import com.eru.netty.protocol.Packet;
import com.eru.netty.protocol.PacketCodeC;
import com.eru.netty.protocol.request.LoginRequestPacket;
import com.eru.netty.protocol.request.MessageRequestPacket;
import com.eru.netty.protocol.response.LoginResponsePacket;
import com.eru.netty.protocol.response.MessageResponsePacket;
import com.sun.deploy.xml.XMLAttributeBuilder;
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

        ByteBuf buffer = (ByteBuf)msg;
        Packet packet = PacketCodeC.INSTANCE.decode(buffer);

        if (packet instanceof LoginRequestPacket){
            // 登录流程
            System.out.println(new Date() + ":收到客户端登录请求.....");
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket)packet;
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());
            if (valid(loginRequestPacket)){
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
        }else if (packet instanceof MessageRequestPacket){
            // 客户端发来消息
            MessageRequestPacket messageRequestPacket = (MessageRequestPacket)packet;

            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            System.out.println(new Date() + ":收到客户端消息【" + messageRequestPacket.getMessage() + "】");
            messageResponsePacket.setMessage("服务端回复: 【" + messageRequestPacket.getMessage() + "】");
            ByteBuf byteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), messageResponsePacket);
            ctx.channel().writeAndFlush(byteBuf);
        }
    }

    private boolean valid(LoginRequestPacket loginRequestPacket){
        return true;
    }
}
