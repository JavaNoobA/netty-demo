package com.eru.netty.protocol.request;

import com.eru.netty.protocol.Packet;
import lombok.Data;

import static com.eru.netty.protocol.command.Command.LOGIN_REQUEST;

/**
 * Created by eru on 2020/3/1.
 */
@Data
public class LoginRequestPacket extends Packet {
    private String userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}
