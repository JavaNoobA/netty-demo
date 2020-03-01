package com.eru.netty.protocol;

import com.eru.netty.serialize.Serializer;
import com.eru.netty.serialize.impl.JSONSerializer;
import io.netty.buffer.ByteBuf;
import org.junit.Assert;
import org.junit.Test;

public class PacketCodeCTest {
    @Test
    public void encode() {
        Serializer serializer = new JSONSerializer();
        LoginRequestPacket requestPacket = new LoginRequestPacket();

        requestPacket.setVersion((byte)1);
        requestPacket.setUserId(123);
        requestPacket.setUsername("eru");
        requestPacket.setPassword("123456");

        PacketCodeC packetCodeC = new PacketCodeC();
        ByteBuf byteBuf = packetCodeC.encode(requestPacket);
        Packet packet = packetCodeC.decode(byteBuf);

        Assert.assertArrayEquals(serializer.serialize(requestPacket), serializer.serialize(packet));
    }

    @Test
    public void decode() {
    }
}
