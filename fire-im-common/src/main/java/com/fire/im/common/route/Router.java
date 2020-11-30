package com.fire.im.common.route;

import java.util.List;

/**
 * @Author: wangzc
 * @Date: 2020/11/25 9:47
 */

public interface Router {

    /**
     * 在一堆服务器里进行路由
     * @param values
     * @param key
     * @return
     */
    String chooseServer(List<String> values, String key);

}
