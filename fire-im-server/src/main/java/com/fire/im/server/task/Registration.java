package com.fire.im.server.task;

import com.fire.im.server.config.AppGlobalConfig;
import com.fire.im.server.utils.SpringBeanHolder;
import com.fire.im.server.utils.ZooUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: wangzc
 * @Date: 2020/11/24 14:34
 */

@Slf4j
public class Registration extends Thread {

    private AppGlobalConfig appConfig;

    private ZooUtil zooUtil;

    private String nodeName;

    public Registration (String nodeName) {
        this.nodeName = nodeName;
        this.appConfig = SpringBeanHolder.getBean(AppGlobalConfig.class);
        this.zooUtil = SpringBeanHolder.getBean(ZooUtil.class);
    }

    @Override
    public void run() {
        //创建父节点
        zooUtil.createPersistentNode(appConfig.getZkRootPath());
        //把自己注册到zk
        if (appConfig.isRegisterSelf()) {

            String path = appConfig.getZkRootPath() + "/" + nodeName;
            //创建临时节点
            zooUtil.createEphemeralNode(path);

            log.info("向命名服务注册本地服务信息成功!");
        }
    }

}
