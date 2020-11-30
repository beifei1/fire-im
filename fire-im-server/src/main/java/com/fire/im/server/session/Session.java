package com.fire.im.server.session;

import lombok.Getter;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @Author: wangzc
 * @Date: 2020/11/30 11:19
 */
@Getter
public class Session<K,V> extends HashMap<K, V> {

    /**
     * session id
     */
    private String sessionId;


    public Session() {
        this.sessionId = UUID.randomUUID().toString();
    }


    public static Session newSession() {
        return new Session();
    }

    /**
     *
     * @param key
     *
     * @param value
     * @return
     */
    public Session putAttribute(K key,V value) {
        super.put(key, value);
        return this;
    }
}
