package com.fire.im.common.route.impl.consistenthash;

import com.fire.im.common.route.Router;
import com.fire.im.common.route.algorithm.TreeMapConsistentHash;

import java.util.List;

/**
 * 一致性hash
 * @Author: wangzc
 * @Date: 2020/11/25 10:17
 */

public class ConsistentHashRouter implements Router {

    private TreeMapConsistentHash hash = new TreeMapConsistentHash();

    @Override
    public String chooseServer(List<String> values, String key) {
        return hash.choose(values,key);
    }
}
