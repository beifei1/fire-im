package com.fire.im.common.route.algorithm;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;

/**
 * 使用红黑树的 tailMap找到比传入节点大的Key
 * @Author: wangzc
 * @Date: 2020/11/25 9:51
 */
public class TreeMapConsistentHash {

    /**
     * 虚拟节点数量
     */
    private static final int VIRTUAL_NODE_SIZE = 4;

    /**
     * 红黑树
     */
    private TreeMap<Long, String> tree = new TreeMap<>();

    /**
     * 定位节点
     * @param values 服务器列表
     * @param key token
     * @return
     */
    public String choose(List<String> values, String key) {
        for (String value: values) {
            add(hash(value), value);
        }

        return selectNode(key);
    }

    /**
     * hash落环
     * @param key
     * @param value
     */
    private void add(long key, String value) {
        tree.clear();
        //虚拟节点
        for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
            Long hash = this.hash("vir" + key + i);
            tree.put(hash, value);
        }
        tree.put(key, value);
    }

    /**
     * 在环中根据传入的值找到第一个server
     * @param value
     * @return
     */
    private String selectNode(String value) {
        long hash = hash(value);
        SortedMap<Long, String> after = tree.tailMap(hash);
        if (after != null && !after.isEmpty()) {

            String server = after.get(after.firstKey());
            System.out.println("路由成功：value: " + value + ", route server: " + server );
            return server;
        }
        return tree.firstEntry().getValue();
    }

    /**
     * hash计算
     * @param value
     * @return
     */
    private Long hash(String value) {
        byte[] digest = DigestUtils.md5(value);

        // hash code, Truncate to 32-bits
        long hashCode = ((long) (digest[3] & 0xFF) << 24) | ((long) (digest[2] & 0xFF) << 16) | ((long) (digest[1] & 0xFF) << 8) | (digest[0] & 0xFF);

        long truncateHashCode = hashCode & 0xffffffffL;
        return truncateHashCode;
    }
}
