package com.fire.im.server.utils;

import com.fire.im.route.api.RouteAPI;
import com.fire.im.route.api.pojo.dto.UserOfflineDTO;
import com.fire.im.server.session.SessionHolder;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @Author: wangzc
 * @Date: 2020/11/26 15:38
 */
@Slf4j
@Component
public class ServerUtil {

    @Autowired
    private RouteAPI routeAPI;

    @Autowired
    private SessionHolder sessionStore;

    /**
     *
     * @param userIds
     */
    public void offline(List<String> userIds) {
        userIds.forEach(userId -> {
            //清除session
            sessionStore.deleteSession(userId);
            log.info("清理服务器用户{}session信息", userId);
        });
        //通知路由服务用户下线
        routeOffline(userIds);
    }

    /**
     *
     * @param userIds
     */
    private void routeOffline(List<String> userIds) {
        try {
            routeAPI.offline(UserOfflineDTO.builder().userIds(userIds).build());
        } catch (Exception e) {
            log.error("user offline error,userId value:{}, msg: {}", userIds.toString(), e.getMessage());
        }
        log.info("通知路由服务用户下线! userId: {}", userIds.toString());
    }

}
