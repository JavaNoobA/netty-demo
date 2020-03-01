package com.eru.netty.protocol.response;

import com.eru.netty.protocol.Packet;
import lombok.Data;

import static com.eru.netty.protocol.command.Command.LOGIN_RESPONSE;

/**
 * Created by eru on 2020/3/1.
 */
@Data
public class LoginResponsePacket extends Packet {
    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}
