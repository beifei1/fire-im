package com.fire.im.server.server.handler;

import com.fire.im.common.handler.HeartBeatHandler;
import com.fire.im.common.utils.NettyAttrUtil;
import com.fire.im.server.config.AppGlobalConfig;
import com.fire.im.server.session.SessionHolder;
import com.fire.im.server.utils.ServerUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author: wangzc
 * @Date: 2020/11/24 15:59
 */
@Slf4j
@Component
public class ServerHeartBeatHandler implements HeartBeatHandler {

    @Autowired
    private AppGlobalConfig appConfig;

    @Autowired
    private ServerUtil serverUtil;

    @Autowired
    private SessionHolder sessionStore;

    @Override
    public void process(ChannelHandlerContext context) {
        //配置的心跳时长
        long heartBeatTime = appConfig.getHeartbeatTime() * 1000;
        //获取最新一次读取时长
        Long lastReaderTime = NettyAttrUtil.getReaderTime(context.channel());
        //当前时间
        long now = System.currentTimeMillis();
        //判断是否已经超时
        if (!Objects.isNull(lastReaderTime) && now - lastReaderTime > heartBeatTime) {

            String userId = sessionStore.getUserId((NioSocketChannel) context.channel());

            if (Objects.nonNull(userId)) {
                log.warn("客户端 {}心跳超时 {}ms,需要关闭连接", userId, now - lastReaderTime);
            }
            //下线用户
            serverUtil.offline(userId);
            context.channel().close();
        }
    }
}
