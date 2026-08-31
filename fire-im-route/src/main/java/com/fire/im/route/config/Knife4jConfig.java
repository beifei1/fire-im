package com.fire.im.route.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3 / Knife4j 4.x 配置
 *
 * @Author: wangzc
 * @Date: 2020/10/28 8:57
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI defaultApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("路由服务接口")
                        .description("路由服务接口")
                        .termsOfService("empty.com")
                        .contact(new Contact()
                                .name("name")
                                .url("url")
                                .email("email"))
                        .version("1.0"));
    }
}
