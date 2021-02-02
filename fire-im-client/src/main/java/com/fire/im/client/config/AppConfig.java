package com.fire.im.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: wangzc
 * @Date: 2020/11/26 16:19
 */
@Data
@Component
@ConfigurationProperties(prefix = "fire-im.client")
public class AppConfig {

    /**
     * 路由服务地址
     */
    private String routeUrl;

    /**
     * 当前客户端的用户名
     */
    private String username;

    /**
     * 当前客户端的密码
     */
    private String password;

}
