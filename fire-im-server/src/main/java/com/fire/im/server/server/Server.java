package com.fire.im.server.server;

import com.fire.im.common.exceptions.IMException;
import com.fire.im.common.protocol.ImMessage;
import com.fire.im.common.utils.Constants;
import com.fire.im.server.api.pojo.PushMessageDTO;
import com.fire.im.server.config.AppGlobalConfig;
import com.fire.im.server.server.initializer.ServerInitializer;
import com.fire.im.server.session.SessionHolder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * @Author: wangzc
 * @Date: 2020/11/23 15:45
 */

@Slf4j
@Component
public class Server {

    @Autowired
    private AppGlobalConfig appConfig;

    @Autowired
    private SessionHolder sessionStore;

    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup work = new NioEventLoopGroup();

    /**
     * 初始化im server并启动
     */
    @SneakyThrows
    @PostConstruct
    public void init () {

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(appConfig.getNettyPort()))
                .childOption(ChannelOption.SO_KEEPALIVE,true)
//                .childOption(ChannelOption.SO_TIMEOUT, 0)
                .childHandler(new ServerInitializer());

        ChannelFuture future = bootstrap.bind().sync();

        if (future.isSuccess()) {
            log.info("启动IM服务器成功！");
        }
    }

    /**
     * 优雅关机
     */
    @PreDestroy
    public void destroy() {
        //主线程同步等待线程池销毁
        boss.shutdownGracefully().syncUninterruptibly();
        work.shutdownGracefully().syncUninterruptibly();

        log.info("服务器已关闭!!!!");
    }

    /**
     * 向指定客户端发送消息
     * @param message
     * @throws IMException
     */
    public void pushMessage (PushMessageDTO message) throws IMException {

        NioSocketChannel socketChannel = sessionStore.getSessionChannel(message.getUserId());

        if (Objects.isNull(socketChannel)) {
            log.error("推送消息失败,客户端已下线。token: {}", message.getUserId());
            throw new IMException("client offline !");
        }

        ImMessage.RequestMessage protoBuf = ImMessage.RequestMessage.newBuilder()
                .setToken("")
                .setUserId(message.getFromUserId())
                .setMsg(message.getMessage())
                .setType(Constants.CommandType.MSG)
                .build();

        ChannelFuture future = socketChannel.writeAndFlush(protoBuf);

        future.addListener((ChannelFutureListener) listener -> {
            log.info("推送消息成功,消息内容: {}", message.toString());
        });
    }

}
