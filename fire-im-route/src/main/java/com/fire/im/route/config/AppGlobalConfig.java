package com.fire.im.route.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: wangzc
 * @Date: 2020/11/24 17:13
 */
@Data
@Component
@ConfigurationProperties(prefix = "fire-im.route")
public class AppGlobalConfig {

    private String zkAddress;

    private Integer zkConnectionTimeout;

    private String zkRootPath;

    private String routeStrategy;

    private String tokenSecret;

}
