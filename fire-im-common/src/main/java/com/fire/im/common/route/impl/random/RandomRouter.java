package com.fire.im.common.route.impl.random;

import com.fire.im.common.exceptions.IMException;
import com.fire.im.common.route.Router;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机路由
 * @Author: wangzc
 * @Date: 2020/11/25 10:21
 */

public class RandomRouter implements Router {
    @Override
    public String chooseServer(List<String> values, String key) {
        int size = values.size();
        if (size == 0) {
            throw new IMException("servers is not available");
        }
        int offset = ThreadLocalRandom.current().nextInt(size);
        return values.get(offset);
    }
}
