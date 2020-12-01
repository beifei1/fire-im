package com.fire.im.server.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: wangzc
 * @Date: 2020/11/23 16:15
 */
@Data
@Component
@EqualsAndHashCode
@ConfigurationProperties(prefix = "fire-im.server")
public class AppGlobalConfig {

    private int nettyPort;

    private String zkAddress;

    private int zkConnectionTimeout;

    private boolean registerSelf;

    private String zkRootPath;

    private int heartbeatTime;

    private String storeType;

    private String routeUrl;

    private String tokenSecret;

    private Integer weight;

}
