package com.eru.netty.protocol.request;

import com.eru.netty.protocol.Packet;
import lombok.Data;

import static com.eru.netty.protocol.command.Command.MESSAGE_REQUEST;

/**
 * Created by eru on 2020/3/1.
 */
@Data
public class MessageRequestPacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
