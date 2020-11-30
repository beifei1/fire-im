package com.fire.im.common.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Author: wangzc
 * @Date: 2020/11/24 15:57
 */

public interface HeartBeatHandler {

    void process(ChannelHandlerContext context);

}
