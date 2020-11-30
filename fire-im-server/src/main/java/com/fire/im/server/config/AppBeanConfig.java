package com.fire.im.server.config;

import com.fire.im.route.api.RouteAPI;
import com.fire.im.server.session.Session;
import com.fire.im.server.session.SessionHolder;
import com.fire.im.server.session.memory.InMemorySessionHolder;
import com.fire.im.server.task.Registration;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 注册bean
 * @Author: wangzc
 * @Date: 2020/11/23 16:09
 */
@Slf4j
@Configuration
public class AppBeanConfig {

    @Autowired
    private AppGlobalConfig appConfig;

    @Value("${server.port}")
    private String httpServerPort;

    /**
     * zkClient
     * @return
     */
    @Bean
    ZkClient zkClient () {
        return new ZkClient(appConfig.getZkAddress(), appConfig.getZkConnectionTimeout());
    }

    /**
     * 向服务器注册自己
     * @return
     */
    @Bean
    CommandLineRunner runner() {
        return args -> {
            log.info("开始向命名服务注册本地服务信息...");

            //节点名称
            String addr = InetAddress.getLocalHost().getHostAddress();
            String nodeName = "ip-" + addr + ":" + appConfig.getNettyPort() + ":" + httpServerPort + ":" + appConfig.getWeight();

            //声明thread
            Registration registration = new Registration(nodeName);
            registration.setName("fire-im-server-registry-thread-");

            //使用线程池调度
            ExecutorService pool = Executors.newFixedThreadPool(1);
            pool.execute(registration);
        };
    }

    /**
     * route api
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
     * 本地缓存
     * @return
     */
    @Bean(name = "localSessionStore")
    public LoadingCache<String, Session> loadingCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(100).build(new CacheLoader<String, Session>() {
                    @Override
                    public Session load(String s) throws Exception {
                        return null;
                    }
                });
    }


    /**
     * session store
     * @return
     */
    @Bean
    SessionHolder memorySessionStore() {
        return new InMemorySessionHolder();
    }
}
