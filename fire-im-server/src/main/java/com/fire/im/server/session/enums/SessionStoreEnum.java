package com.fire.im.server.session.enums;

import com.fire.im.server.session.SessionHolder;
import com.fire.im.server.session.memory.InMemorySessionHolder;
import com.fire.im.server.session.redis.InRedisSessionHolder;
import com.fire.im.server.utils.SpringBeanHolder;
import lombok.Getter;

/**
 * @Author: wangzc
 * @Date: 2020/11/24 13:59
 */
@Getter
@Deprecated
public enum SessionStoreEnum {

    MEMORY(StoreTypeEnum.MEMORY, SpringBeanHolder.getBean(InMemorySessionHolder.class)),

    REDIS(StoreTypeEnum.REDIS, SpringBeanHolder.getBean(InRedisSessionHolder.class));

    private StoreTypeEnum storeEnum;
    private SessionHolder sessionStore;

    SessionStoreEnum (StoreTypeEnum storeEnum, SessionHolder sessionStore) {
        this.storeEnum = storeEnum;
        this.sessionStore = sessionStore;
    }

    public static SessionHolder getSessionStore(StoreTypeEnum storeEnum) {
        for (SessionStoreEnum value : SessionStoreEnum.values()) {
            if (value.getStoreEnum().equals(storeEnum)) {
                return value.getSessionStore();
            }
        }
        return null;
    }

}
