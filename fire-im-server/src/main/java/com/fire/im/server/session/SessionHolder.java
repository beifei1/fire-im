package com.fire.im.server.session;

import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.List;

/**
 * @Author: wangzc
 * @Date: 2020/11/24 9:38
 */

public interface SessionHolder {

    /**
     * 创建session
     * @param userId
     * @return
     */
    Session saveSession(String userId);

    /**
     * 删除session
     * @param userId
     * @return
     */
    boolean deleteSession(String userId);

    /**
     * 为用户的session增加channel属性
     * @param userId
     * @param socketChannel
     * @return
     */
    boolean addSessionChannel(String userId, NioSocketChannel socketChannel);

    /**
     * 根据UserId获取channel
     * @param userId
     * @return
     */
    NioSocketChannel getSessionChannel(String userId);

    /**
     * 根据channel获取userId
     * @param socketChannel
     * @return
     */
    String getUserId(NioSocketChannel socketChannel);

    /**
     * 获取所有在线用户
     * @return
     */
    List<String> getOnlineUserId();

}
