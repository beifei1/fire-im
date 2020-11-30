package com.fire.im.server.api;

import com.fire.im.common.pojo.Response;
import com.fire.im.server.api.pojo.PushMessageDTO;
import feign.Headers;
import feign.RequestLine;

import java.net.URI;

/**
 * @Author: wangzc
 * @Date: 2020/11/24 11:29
 */

public interface ServerAPI {

    /**
     * 发送消息
     * @param message
     * @return
     */
    @RequestLine("POST /pushMsg")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Response pushMessage(URI uri, PushMessageDTO message);


}
