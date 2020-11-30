package com.fire.im.route.service.impl;

import com.fire.im.common.exceptions.IMException;
import com.fire.im.common.pojo.Response;
import com.fire.im.common.pojo.dto.Server;
import com.fire.im.route.service.IMessageService;
import com.fire.im.route.service.IRouteService;
import com.fire.im.route.utils.RedisUtil;
import com.fire.im.route.utils.RouterUtil;
import com.fire.im.server.api.ServerAPI;
import com.fire.im.server.api.pojo.PushMessageDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.List;

/**
 * @Author: wangzc
 * @Date: 2020/11/27 11:02
 */

@Slf4j
@Service
public class MessageServiceImpl implements IMessageService {

    @Autowired
    ServerAPI serverAPI;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    IRouteService routeService;

    @Override
    public void sendMessage(String message, String fromUserId, String... toUserId) {

        Arrays.stream(toUserId).forEach(userId -> {

            Server server = routeService.getUserRouteInfo(userId);

            if (Objects.isNull(server)) {
                throw new IMException("未找到用户的路由信息");
            }

            String serverFullAddr = "http://" + server.getIp() + ":" + server.getHttpPort();
            Response response = null;
            try {
                response = serverAPI.pushMessage(
                        new URI(serverFullAddr),
                        PushMessageDTO.builder().message(message).fromUserId(fromUserId).userId(userId).build()
                );
            } catch (URISyntaxException e) {
                throw new IMException(e.getMessage());
            }

            log.info("发送消息: fromUser: {}, toUser: {}, message: {}, 路由地址: {}", fromUserId, toUserId, message, serverFullAddr);
            log.info("发送结果: {}", response.toString());
        });
    }

    @Override
    public void broadcastMessage(String fromUserId, String message) {
        //获取所有保存的路由信息
        Set<String> routes = redisUtil.keys(RouterUtil.Consts.ROUTE_INFO_PREFIX + "*");
        //获取对应的路由信息
        routes.stream().forEach(key -> {
            String userId = StringUtils.replace(key, RouterUtil.Consts.ROUTE_INFO_PREFIX, "");
            log.info("群发消息,userId: {}", userId);
            Server server = routeService.getUserRouteInfo(userId);

            if (Objects.isNull(server)) {
                throw new IMException("未找到用户的路由信息");
            }

            String serverFullAddr = "http://" + server.getIp() + ":" + server.getHttpPort();
            Response response = null;
            try {
                response = serverAPI.pushMessage(
                        new URI(serverFullAddr),
                        PushMessageDTO.builder().message(message).fromUserId(fromUserId).userId(userId).build()
                );
            } catch (URISyntaxException e) {
                throw new IMException(e.getMessage());
            }

            log.info("发送消息: fromUser: {}, toUser: {}, message: {}, 路由地址: {}", fromUserId, userId, message, serverFullAddr);
            log.info("发送结果: {}", response.toString());
        });
    }
}
