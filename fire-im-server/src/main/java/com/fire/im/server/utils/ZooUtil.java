package com.fire.im.server.utils;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: wangzc
 * @Date: 2020/11/24 14:26
 */
@Slf4j
@Component
public class ZooUtil {

    @Autowired
    private ZkClient zkClient;

    /**
     * 创建持久化节点
     * @param path
     */
    public void createPersistentNode (String path) {
        boolean exists = zkClient.exists(path);
        if (!exists) {
            zkClient.createPersistent(path);
        }
    }

    /**
     * 创建临时节点
     * @param path
     */
    public void createEphemeralNode(String path) {
        zkClient.createEphemeral(path);
    }
}
