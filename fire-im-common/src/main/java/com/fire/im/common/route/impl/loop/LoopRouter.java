package com.fire.im.common.route.impl.loop;

import com.fire.im.common.exceptions.IMException;
import com.fire.im.common.route.Router;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 轮询路由实现
 * @Author: wangzc
 * @Date: 2020/11/25 10:19
 */

public class LoopRouter implements Router {

    private AtomicLong index = new AtomicLong();

    @Override
    public String chooseServer(List<String> values, String key) {
        if (values.size() == 0) {
            throw new IMException("servers is not available");
        }
        Long pos = index.incrementAndGet() % values.size();
        if (pos < 0) {
            pos = 0L;
        }
        return values.get(pos.intValue());
    }

}
