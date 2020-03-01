package com.eru.netty.protocol.response;

import com.eru.netty.protocol.Packet;
import lombok.Data;

import static com.eru.netty.protocol.command.Command.MESSAGE_RESPONSE;

/**
 * Created by eru on 2020/3/1.
 */
@Data
public class MessageResponsePacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_RESPONSE;
    }
}
