package com.fire.im.route.service.impl;

import com.fire.im.common.exceptions.IMException;
import com.fire.im.common.pojo.dto.Server;
import com.fire.im.route.api.pojo.dto.UserOfflineDTO;
import com.fire.im.route.service.IRouteService;
import com.fire.im.route.utils.RedisUtil;
import com.fire.im.route.utils.RouterUtil;
import com.fire.im.route.utils.ServerHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Objects;

/**
 * @Author: wangzc
 * @Date: 2020/11/25 16:56
 */

@Slf4j
@Service
public class RouteServiceImpl implements IRouteService {

    @Autowired
    ServerHolder serverHolder;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public void isServerAvailable(Server server) {
        boolean available = RouterUtil.isConnectable(server.getIp(), server.getSocketPort(), 2000);

        if (!available) {
            log.error("ip: {}, port: {} is not available!");
            serverHolder.reBuildServers();

            throw new IMException("server is not available!");
        }
    }

    @Override
    public void saveRouteInfo(String userId, String msg) {
        redisUtil.set(RouterUtil.Consts.ROUTE_INFO_PREFIX + userId, msg);
    }

    @Override
    public void userOffline(UserOfflineDTO param) {
        redisUtil.del(RouterUtil.Consts.ROUTE_INFO_PREFIX + param.getUserId());
    }

    @Override
    public Server getUserRouteInfo(String userId) {
        Object obj = redisUtil.get(RouterUtil.Consts.ROUTE_INFO_PREFIX + userId);

        if (Objects.isNull(obj)) {
            return null;
        }
        String serverStr = String.valueOf(obj);

        return RouterUtil.parse(serverStr);
    }
}
