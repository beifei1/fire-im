package com.fire.im.route.service;

import com.fire.im.common.pojo.dto.Server;
import com.fire.im.route.api.pojo.dto.UserOfflineDTO;

/**
 * @Author: wangzc
 * @Date: 2020/11/25 16:56
 */

public interface IRouteService {

    /**
     * 检查server是否可达
     * @param server
     */
    void isServerAvailable(Server server);

    /**
     * 保存用户路由信息
     * @param userId
     * @param server
     */
    void saveRouteInfo(String userId, String server);

    /**
     * 用户下线
     * @param param
     */
    void userOffline(UserOfflineDTO param);

    /**
     * 根据用户Id获取路由信息
     * @param userId
     * @return
     */
    Server getUserRouteInfo(String userId);

}
