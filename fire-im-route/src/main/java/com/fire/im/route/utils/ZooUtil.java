package com.fire.im.route.utils;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @Author: wangzc
 * @Date: 2020/11/25 10:52
 */
@Slf4j
@Component
public class ZooUtil {

    @Autowired
    private ZkClient zkClient;

    @Autowired
    private ServerHolder serverHolder;

    /**
     * 监听子节点事件
     * @param path
     */
    public void subscribeEvent(String path) {
        zkClient.subscribeChildChanges(path, (parentPath,currentChilds) -> {
            log.info("清除并更新本地server缓存,parentPath: {}, childs: {}" ,parentPath, currentChilds.toString());
            //更新本地缓存
            serverHolder.updateServer(currentChilds);
        });
    }

    /**
     * 获取所有子节点
     * @param parentPath
     * @return
     */
    public List<String> getAllChildren(String parentPath) {
        return zkClient.getChildren(parentPath);
    }
}
