package com.fire.im.client.client.handler;

import com.fire.im.common.protocol.ImMessage;
import com.fire.im.common.utils.Constants;
import com.fire.im.common.utils.NettyAttrUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * @Author: wangzc
 * @Date: 2020/11/26 16:25
 */
@Slf4j
@ChannelHandler.Sharable
public class ImClientSimpleInboundHandler extends SimpleChannelInboundHandler<ImMessage.RequestMessage> {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //TODO 断线重连逻辑
        super.channelInactive(ctx);
    }

    //test
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ImMessage.RequestMessage msg) throws Exception {
        InetSocketAddress addr = (InetSocketAddress) ctx.channel().remoteAddress();
        log.info("【读取消息】 - From {}, Type:{}", addr.getAddress().getHostAddress(), msg.getType());
        if (msg.getType() == Constants.CommandType.HEARTBEAT) {
            NettyAttrUtil.updateReaderTime(ctx.channel(), System.currentTimeMillis());
        }

        //直接打印消息
        if (msg.getType() != Constants.CommandType.HEARTBEAT) {
            System.err.println("收到消息,from: " + msg.getUserId() + ", msg: " + msg.getMsg());
        }
    }

    /**
     * 10s没有write触发
     * @see IdleStateHandler
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                log.info("向服务器发送心跳消息!");
                //向服务器发送ping消息
                ImMessage.RequestMessage heart = ImMessage.RequestMessage.newBuilder()
                        .setToken("ping")
                        .setMsg("ping")
                        .setUserId("ping")
                        .setType(Constants.CommandType.HEARTBEAT)
                        .build();
                ctx.writeAndFlush(heart).addListeners((ChannelFutureListener) future -> {
                    if (!future.isSuccess()) {
                        log.error("IO error,close Channel");
                        future.channel().close();
                    }
                });
            }

        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("im server connect success!");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常时断开连接
        cause.printStackTrace() ;
        ctx.close() ;
    }
}
