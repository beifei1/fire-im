package com.fire.im.server.session.holder;

import com.fire.im.server.session.Session;
import com.fire.im.server.session.SessionHolder;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.List;

/**
 * 使用redis map数据结构
 * @Author: wangzc
 * @Date: 2020/11/24 10:26
 */
public class InRedisSessionHolder implements SessionHolder {
    @Override
    public Session saveSession(String userId) {
        return null;
    }

    @Override
    public boolean deleteSession(String userId) {
        return false;
    }

    @Override
    public boolean addSessionChannel(String userId, NioSocketChannel socketChannel) {
        return false;
    }

    @Override
    public NioSocketChannel getSessionChannel(String userId) {
        return null;
    }

    @Override
    public String getUserId(NioSocketChannel socketChannel) {
        return null;
    }

    @Override
    public List<String> getOnlineUserId() {
        return null;
    }
}
