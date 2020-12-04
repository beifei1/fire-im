package com.fire.im.route.config;

import com.fire.im.common.route.Router;
import com.fire.im.common.utils.SnowflakeId;
import com.fire.im.route.task.Subscription;
import com.fire.im.server.api.ServerAPI;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: wangzc
 * @Date: 2020/11/25 10:39
 */
@Slf4j
@Configuration
public class AppBeanConfig {

    @Autowired
    private AppGlobalConfig appConfig;

    /**
     * zkclient
     * @return
     */
    @Bean
    public ZkClient zkClient() {
        return new ZkClient(appConfig.getZkAddress(), appConfig.getZkConnectionTimeout());
    }

    /**
     * guava本地缓存
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

    /**
     * redis
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 路由策略
     * @return
     */
    @Bean
    @SneakyThrows
    public Router router() {
        String routeWay = appConfig.getRouteStrategy();
        return (Router) Class.forName(routeWay).newInstance();
    }

    /**
     * 应用启动时订阅节点信息
     * @return
     */
    @Bean
    public CommandLineRunner subscribeCommand() {
        return args -> {
            //使用线程池调度
            ExecutorService pool = Executors.newFixedThreadPool(1);
            pool.execute(new Subscription());
        };
    }

    /**
     * feign server api
     * @return
     */
    @Bean
    public ServerAPI serverAPI() {
         return  Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(1000, 3500))
                .retryer(new Retryer.Default(5000, 5000, 3))
                .target(ServerAPI.class, "http://127.0.0.1:1111");
    }

    /**
     * 雪花Id
     * @return
     */
    @Bean
    SnowflakeId snowflakeId() {
        return new SnowflakeId(appConfig.getSnowflakeMachineId().longValue(),appConfig.getSnowflakeDatacenterId().longValue());
    }
}
