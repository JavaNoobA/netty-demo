package com.eru.netty.attribute;

import io.netty.util.AttributeKey;

/**
 * Created by eru on 2020/3/1.
 */
public interface Attributes {
    AttributeKey<Boolean> LOGIN =  AttributeKey.newInstance("login");
}
