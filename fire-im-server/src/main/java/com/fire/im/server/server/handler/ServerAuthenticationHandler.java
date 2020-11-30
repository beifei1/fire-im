package com.fire.im.server.server.handler;

import com.fire.im.common.protocol.ImMessage;
import com.fire.im.common.utils.Constants;
import com.fire.im.common.utils.NettyAttrUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 鉴权逻辑
 * @Author: wangzc
 * @Date: 2020/11/24 9:47
 */
@ChannelHandler.Sharable
public class ServerAuthenticationHandler extends SimpleChannelInboundHandler<ImMessage.RequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ImMessage.RequestMessage msg) throws Exception {
        //如果是非登录请求,则判断channel是否已经鉴权,如果未鉴权，关闭channel，否则进入下一个handler
        if (msg.getType() != Constants.CommandType.LOGIN) {
            if (NettyAttrUtil.isAuthentication(ctx.channel()) == false) {
                ctx.channel().close();
            }
        }
        ctx.fireChannelRead(msg);
    }
}
