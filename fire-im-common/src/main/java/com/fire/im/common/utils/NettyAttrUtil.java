package com.fire.im.common.utils;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.util.Objects;

/**
 * @Author: wangzc
 * @Date: 2020/11/24 16:02
 */

public class NettyAttrUtil {

    private static final AttributeKey<String> ATTR_KEY_READER_TIME = AttributeKey.valueOf("readerTime");

    private static final AttributeKey<Boolean> ATTR_KEY_AUTHENTICATION = AttributeKey.valueOf("isAuthentication");

    public static void updateReaderTime(Channel channel, Long time) {
        channel.attr(ATTR_KEY_READER_TIME).set(time.toString());
    }

    public static Long getReaderTime(Channel channel) {
        String value = getAttribute(channel, ATTR_KEY_READER_TIME);

        if (value != null) {
            return Long.valueOf(value);
        }
        return null;
    }

    private static String getAttribute(Channel channel, AttributeKey<String> key) {
        Attribute<String> attr = channel.attr(key);
        return attr.get();
    }

    public static void authentication(Channel channel) {
        channel.attr(ATTR_KEY_AUTHENTICATION).set(true);
    }

    public static Boolean isAuthentication(Channel channel) {
        Boolean auth = channel.attr(ATTR_KEY_AUTHENTICATION).get();
        if (Objects.isNull(auth)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
