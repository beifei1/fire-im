package com.fire.im.server.session.memory;

import com.fire.im.server.session.Session;
import com.fire.im.server.session.SessionHolder;
import com.fire.im.server.utils.Consts;
import com.google.common.cache.LoadingCache;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: wangzc
 * @Date: 2020/11/24 10:26
 */
public class InMemorySessionHolder implements SessionHolder {

    @Resource(name = "localSessionStore")
    private LoadingCache<String, Session> loadingCache;

    @Override
    public Session saveSession(String userId) {
        Session session = Session.newSession();
        session.putAttribute(Consts.SessionConsts.USER_ID, userId);

        loadingCache.put(userId, session);
        return session;
    }


    @Override
    @SneakyThrows
    public boolean deleteSession(String userId) {
        if (Objects.isNull(loadingCache.get(userId))) {
            return Boolean.FALSE;
        }
        loadingCache.invalidate(userId);
        return Boolean.TRUE;
    }


    @Override
    @SneakyThrows
    public boolean addSessionChannel(String userId, NioSocketChannel socketChannel) {
        if (Objects.isNull(loadingCache.get(userId))) {
            return Boolean.FALSE;
        }
        Session session = loadingCache.get(userId);
        session.put(Consts.SessionConsts.SOCKET_CHANNEL, socketChannel);
        return Boolean.TRUE;
    }


    @Override
    @SneakyThrows
    public NioSocketChannel getSessionChannel(String userId) {
        if (Objects.isNull(loadingCache.get(userId))) {
            return null;
        }
        Session session = loadingCache.get(userId);
        Object obj = session.get(Consts.SessionConsts.SOCKET_CHANNEL);
        if (Objects.isNull(obj)) {
            return null;
        }
        return (NioSocketChannel)obj;
    }


    @Override
    public String getUserId(NioSocketChannel channel) {
        Map<String, Session> map = loadingCache.asMap();
        for (Map.Entry<String, Session> entry : map.entrySet()) {
            Session session = entry.getValue();
            if (session.get(Consts.SessionConsts.SOCKET_CHANNEL) == channel) {
                return entry.getKey();
            }
        }
        return StringUtils.EMPTY;
    }

}
