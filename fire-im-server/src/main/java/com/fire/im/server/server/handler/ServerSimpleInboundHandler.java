package com.fire.im.server.server.handler;

import com.fire.im.common.handler.HeartBeatHandler;
import com.fire.im.common.protocol.ImMessage;
import com.fire.im.common.utils.Constants;
import com.fire.im.common.utils.NettyAttrUtil;
import com.fire.im.server.config.AppGlobalConfig;
import com.fire.im.server.session.SessionHolder;
import com.fire.im.server.utils.ServerUtil;
import com.fire.im.server.utils.SpringBeanHolder;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: wangzc
 * @Date: 2020/11/23 17:11
 */
@Slf4j
@ChannelHandler.Sharable
public class ServerSimpleInboundHandler extends SimpleChannelInboundHandler<ImMessage.RequestMessage> {

    /**
     * 断开连接时通知用户下线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String userId = SpringBeanHolder.getBean(SessionHolder.class).getUserId((NioSocketChannel) ctx.channel());

        if (StringUtils.isNotBlank(userId)) {
            log.info("连接断开, 准备用户{}下线", userId);
            SpringBeanHolder.getBean(ServerUtil.class).offline(userId);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ImMessage.RequestMessage msg) throws Exception {
        //处理登录请求
        if (msg.getType() == Constants.CommandType.LOGIN) {
            String secret = SpringBeanHolder.getBean(AppGlobalConfig.class).getTokenSecret();
            //计算出的token
            String token = DigestUtils.md5Hex(msg.getUserId() + secret);
            if (!StringUtils.equals(token, msg.getToken())) {
                log.info("用户鉴权失败,userId: {}, token: {}",msg.getUserId(), msg.getToken());
                ctx.channel().close();
            } else {
                //标识为已鉴权
                log.info("用户鉴权成功,userId: {}, token: {}",msg.getUserId(), msg.getToken());
                NettyAttrUtil.authentication(ctx.channel());
            }

            //保存客户端和channel关系
            SessionHolder sessionStore = SpringBeanHolder.getBean(SessionHolder.class);
            sessionStore.saveSession(msg.getUserId());
            sessionStore.addSessionChannel(msg.getUserId(), (NioSocketChannel)ctx.channel());

            log.info("客户端 {} online success!!!", msg.getUserId());
        }

        //处理心跳消息
        if (msg.getType() == Constants.CommandType.HEARTBEAT){
            NettyAttrUtil.updateReaderTime(ctx.channel(),System.currentTimeMillis());
            //向客户端响应 pong 消息
            ImMessage.RequestMessage heart = ImMessage.RequestMessage.newBuilder()
                    .setToken("pong")
                    .setMsg("pong")
                    .setUserId("pong")
                    .setType(Constants.CommandType.HEARTBEAT)
                    .build();
            ctx.writeAndFlush(heart).addListeners((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    log.error("io error, close channel!!!");
                    future.channel().close();
                } else {
                    log.info("响应心跳信息到客户端成功!");
                }
            }) ;
        }

    }

    /**
     * 超时未写会触发一个IdleStateEvent事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //处理IdleStateEvent
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent)evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {

                log.info("检测客户端是否存活");

                HeartBeatHandler heartBeatHandler = SpringBeanHolder.getBean(ServerHeartBeatHandler.class);
                heartBeatHandler.process(ctx);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}