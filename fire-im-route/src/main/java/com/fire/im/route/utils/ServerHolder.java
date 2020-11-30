package com.fire.im.route.utils;

import com.fire.im.route.config.AppGlobalConfig;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
/**
 * @Author: wangzc
 * @Date: 2020/11/25 10:54
 */
@Slf4j
@Component
public class ServerHolder {

    @Autowired
    private AppGlobalConfig appConfig;

    @Autowired
    private ZooUtil zooUtil;

    @Autowired
    private LoadingCache<String, String> localCache;

    /**
     * 增加server
     * @param k
     * @param v
     */
    public void addServer(String k,String v) {
        localCache.put(k,v);
    }

    /**
     * 更新server
     * @param servers
     */
    public void updateServer(List<String> servers) {
        localCache.invalidateAll();
        servers.stream().forEach(server -> {
            //ip-127.0.0.1:11212:9082:weight or 127.0.0.1:11212:9082:weight
            String key ;
            if (server.split("-").length == 2){
                key = server.split("-")[1];
            }else {
                key = server ;
            }
            addServer(key, key);
        });
    }

    /**
     * 查询所有在线server
     * @return
     */
    public List<String> getAll() {
        List<String> servers = Lists.newArrayList();
        if (localCache.size() == 0) {
            List<String> allChild = zooUtil.getAllChildren(appConfig.getZkRootPath());
            allChild.forEach(node -> {
                String server = node.split("-")[1];
                addServer(server,server);
            });
        }
        localCache.asMap().forEach((k,v) -> {
            servers.add(k);
        });

        return servers;
    }

    /**
     * 重新获取server列表
     */
    public void reBuildServers() {updateServer(getAll());}
}
