package com.fire.im.common.route.impl.last;

import com.fire.im.common.route.Router;

import java.util.List;
import java.util.TreeMap;

/**
 * 最后一个
 * @Author: wangzc
 * @Date: 2020/11/30 10:36
 */

public class LastRouter implements Router {

    private TreeMap<Integer,String> map = new TreeMap<>();

    @Override
    public String chooseServer(List<String> values, String key) {

        values.stream().forEach(server -> {
            Integer weight = Integer.valueOf(server.split(":")[3]);

            map.put(weight, server);
        });
        return map.get(map.lastKey());
    }
}
