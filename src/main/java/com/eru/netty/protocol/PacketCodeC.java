package com.eru.netty.protocol;

import com.eru.netty.serialize.Serializer;
import com.eru.netty.serialize.impl.JSONSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

import static com.eru.netty.protocol.command.Command.LOGIN_REQUEST;

/**
 * Created by eru on 2020/3/1.
 */
public class PacketCodeC {
    private static final int MAGIC_NUMBER = 0x12345678;
    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private static final Map<Byte, Serializer> serializerMap;

    static {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlogrithm(), serializer);
    }

    public ByteBuf encode(Packet packet){
        // 1. 创建buf对象
        ByteBuf buf = ByteBufAllocator.DEFAULT.ioBuffer();
        // 2. 序列化java对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 3. 实际编码过程
        buf.writeInt(MAGIC_NUMBER);
        buf.writeByte(packet.getVersion());
        buf.writeByte(Serializer.DEFAULT.getSerializerAlogrithm());
        buf.writeByte(packet.getCommand());
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);

        return buf;
    }

    public Packet decode(ByteBuf byteBuf){
        // 跳过magicNumber
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法
        byte serializerAlog = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType =  getRequestType(command);
        Serializer serializer = getSerializer(serializerAlog);

        if (requestType != null && serializer != null){
            return serializer.deserialize(requestType, bytes);
        }
        return null;
    }

    private Serializer getSerializer(byte serializerAlog) {
        return serializerMap.get(serializerAlog);
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }
}
