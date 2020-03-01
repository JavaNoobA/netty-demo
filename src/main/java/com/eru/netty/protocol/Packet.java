package com.eru.netty.protocol;

import lombok.Data;

/**
 * Created by eru on 2020/3/1.
 */
@Data
public abstract class Packet {

    /**
     * 协议版本
     */
    private Byte version = 1;

    /**
     * 指令
     * @return 结果
     */
    public abstract Byte getCommand();
}
