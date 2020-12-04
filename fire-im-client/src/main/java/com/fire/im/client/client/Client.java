package com.fire.im.client.client;

import com.fire.im.client.client.initialzer.ClientHandlerInitialzer;
import com.fire.im.client.config.AppConfig;
import com.fire.im.common.pojo.Response;
import com.fire.im.common.protocol.ImMessage;
import com.fire.im.common.utils.Constants;
import com.fire.im.route.api.RouteAPI;
import com.fire.im.route.api.pojo.dto.UserLoginDTO;
import com.fire.im.route.api.pojo.vo.LoginVO;
import com.google.common.cache.LoadingCache;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: wangzc
 * @Date: 2020/11/26 16:17
 */

@Slf4j
@Component
public class Client {

    EventLoopGroup group = new NioEventLoopGroup(0, new DefaultThreadFactory("fire-im"));

    @Autowired
    RouteAPI routeAPI;

    SocketChannel channel;

    @Autowired
    LoadingCache loadingCache;

    @Autowired
    AppConfig appConfig;

    /**
     * 初始化
     */
    @PostConstruct
    private void init() {
        Response<LoginVO> resposne = routeAPI.login(
                UserLoginDTO.builder()
                        .account(appConfig.getUsername())
                        .password(appConfig.getPassword()).build()
        );

        log.info("用户登录结果: {}", resposne.toString());
        //启动客户端
        startClient(resposne.getData());
        //向服务器进行注册
        loginImServer(resposne.getData().getToken(), resposne.getData().getUserId());
        //把客户端信息保存到本地缓存
        loadingCache.put("token", resposne.getData().getToken());
        loadingCache.put("userId", resposne.getData().getUserId());
    }

    /**
     * 启动client
     * @param loginVO
     */
    @SneakyThrows
    private void startClient(LoginVO loginVO) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientHandlerInitialzer())
        ;

        ChannelFuture future = bootstrap.connect(loginVO.getIp(), loginVO.getSocketPort()).sync();
        if (future.isSuccess()) {
            log.info("启动IM Client成功");
        }
        //TODO 客户端断线重连
        channel = (SocketChannel) future.channel();
    }


    /**
     * login server
     */
    private void loginImServer(String token,String userId) {
        ImMessage.RequestMessage login = ImMessage.RequestMessage.newBuilder()
                .setToken(token)
                .setMsg("login")
                .setUserId(userId)
                .setType(Constants.CommandType.LOGIN)
                .build();
        ChannelFuture future = channel.writeAndFlush(login);
        future.addListener((ChannelFutureListener) channelFuture -> System.out.println("login server success!!!"));
    }

}
