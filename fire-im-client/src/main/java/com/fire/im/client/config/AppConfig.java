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

    private String routeUrl;

    private String username;

    private String password;

}
