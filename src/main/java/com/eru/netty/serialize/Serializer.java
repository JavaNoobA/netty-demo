package com.eru.netty.serialize;

import com.eru.netty.serialize.impl.JSONSerializer;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * Created by eru on 2020/3/1.
 */
public interface Serializer {

    Serializer DEFAULT =  new JSONSerializer();

    /**
     * 序列化算法
     * @return
     */
    byte getSerializerAlogrithm();

    /**
     * java对象转换成二进制数据
     * @param object obj
     * @return 结果
     */
    byte[] serialize(Object object);

    /**
     * 二进制数据转换成java对象
     * @param clazz java对象
     * @param bytes 二进制数据
     * @param <T> 指定类
     * @return 结果
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
