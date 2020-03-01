package com.eru.netty.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.eru.netty.serialize.Serializer;
import com.eru.netty.serialize.SerializerAlogrithm;

/**
 * Created by eru on 2020/3/1.
 */
public class JSONSerializer implements Serializer {
    @Override
    public byte getSerializerAlogrithm() {
        return SerializerAlogrithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
