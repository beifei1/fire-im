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

    /**
     * 更新channel的读取时间
     * @param channel
     * @param time
     */
    public static void updateReaderTime(Channel channel, Long time) {
        channel.attr(ATTR_KEY_READER_TIME).set(time.toString());
    }

    /**
     * 获取读取的时间
     * @param channel
     * @return
     */
    public static Long getReaderTime(Channel channel) {
        String value = getAttribute(channel, ATTR_KEY_READER_TIME);

        if (value != null) {
            return Long.valueOf(value);
        }
        return null;
    }

    /**
     * 获取channel属性
     * @param channel
     * @param key
     * @return
     */
    private static String getAttribute(Channel channel, AttributeKey<String> key) {
        Attribute<String> attr = channel.attr(key);
        return attr.get();
    }

    /**
     * 设置channel为已认证状态
     * @param channel
     */
    public static void authentication(Channel channel) {
        channel.attr(ATTR_KEY_AUTHENTICATION).set(true);
    }

    /**
     * 检查channel是否已认证
     * @param channel
     * @return
     */
    public static Boolean isAuthentication(Channel channel) {
        Boolean auth = channel.attr(ATTR_KEY_AUTHENTICATION).get();
        if (Objects.isNull(auth)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
