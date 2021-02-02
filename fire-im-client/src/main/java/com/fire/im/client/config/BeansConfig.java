package com.fire.im.client.config;

import com.fire.im.route.api.RouteAPI;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wangzc
 * @Date: 2020/11/26 16:17
 */

@Configuration
public class BeansConfig {

    @Autowired
    private AppConfig appConfig;

    /**
     * 把feign实例纳入到spring容器管理
     * @return
     */
    @Bean
    RouteAPI routeAPI() {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(1000, 3500))
                .retryer(new Retryer.Default(5000, 5000, 3))
                .target(RouteAPI.class, appConfig.getRouteUrl());
    }

    /**
     * 把loadingcache纳入到spring容器管理
     * @return
     */
    @Bean
    public LoadingCache<String, String> loadingCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(100).build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String s) throws Exception {
                        return null;
                    }
                });
    }

}
