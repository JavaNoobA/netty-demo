package com.eru.netty.util;

import com.eru.netty.attribute.Attributes;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * Created by eru on 2020/3/1.
 */
public class LoginUtil {

    public static void makeAsLogin(Channel channel){
        channel.attr(Attributes.LOGIN).set(true);
    }
    public static boolean hasLogin(Channel channel){
        Attribute<Boolean> loginAttr = channel.attr(Attributes.LOGIN);
        return loginAttr != null;
    }
}
