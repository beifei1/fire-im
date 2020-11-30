package com.fire.im.common.route.impl.first;

import com.fire.im.common.route.Router;

import java.util.List;
import java.util.TreeMap;

/**
 * 第一个
 * @Author: wangzc
 * @Date: 2020/11/30 10:32
 */

public class FirstRouter implements Router {

    private TreeMap<Integer,String> map = new TreeMap<>();

    @Override
    public String chooseServer(List<String> values, String key) {

        values.stream().forEach(server -> {
            Integer weight = Integer.valueOf(server.split(":")[3]);

            map.put(weight, server);
        });
        return map.get(map.firstKey());
    }
}
