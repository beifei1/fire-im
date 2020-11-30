package com.fire.im.route.service;

/**
 * @Author: wangzc
 * @Date: 2020/11/27 11:02
 */

public interface IMessageService {

    /**
     * 发送信息
     * @param message
     * @param fromUserId
     * @param toUserId
     */
    void sendMessage(String message,String fromUserId,String... toUserId);

    /**
     * 发送广播消息
     * @param fromUserId
     * @param message
     */
    void broadcastMessage(String fromUserId, String message);

}
