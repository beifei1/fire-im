package com.fire.im.server.utils;

import com.fire.im.route.api.RouteAPI;
import com.fire.im.route.api.pojo.dto.UserOfflineDTO;
import com.fire.im.server.session.SessionHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
     * 用户下线
     * @param userId
     */
    public void offline(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            //清除session
            sessionStore.deleteSession(userId);
            log.info("清理服务器用户{}session信息", userId);
            //通知路由服务用户下线
            routeOffline(userId);
        }
    }

    /**
     * userId
     * @param userId
     */
    private void routeOffline(String userId) {
        try {
            routeAPI.offline(UserOfflineDTO.builder().userId(userId).build());
        } catch (Exception e) {
            log.error("user offline error,userId value:{}, msg: {}", userId, e.getMessage());
        }
        log.info("通知路由服务用户下线! userId: {}", userId);
    }

}
