package com.fire.im.route.task;

import com.fire.im.route.config.AppGlobalConfig;
import com.fire.im.route.utils.SpringBeanHolder;
import com.fire.im.route.utils.ZooUtil;

/**
 * @Author: wangzc
 * @Date: 2020/11/25 11:14
 */

public class Subscription implements Runnable {

    private ZooUtil zooUtil;

    private AppGlobalConfig appConfig;

    public Subscription() {
        zooUtil = SpringBeanHolder.getBean(ZooUtil.class);
        appConfig = SpringBeanHolder.getBean(AppGlobalConfig.class);
    }

    @Override
    public void run() {
        //注册监听服务
        zooUtil.subscribeEvent(appConfig.getZkRootPath());
    }
}
