package com.fire.im.route.api;

import com.fire.im.common.pojo.Response;
import com.fire.im.common.pojo.Void;
import com.fire.im.route.api.pojo.dto.UserLoginDTO;
import com.fire.im.route.api.pojo.dto.UserOfflineDTO;
import com.fire.im.route.api.pojo.vo.LoginVO;
import feign.Headers;
import feign.RequestLine;

import javax.validation.Valid;

/**
 * @Author: wangzc
 * @Date: 2020/11/25 16:23
 */

public interface RouteAPI {

    /**
     * 用户登录
     * @param message
     * @return
     */
    @RequestLine("POST /user/login")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Response<LoginVO> login(@Valid  UserLoginDTO message);

    /**
     * 用户下线
     * @param param
     * @return
     */
    @RequestLine("POST /user/offline")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Response<Void> offline(@Valid UserOfflineDTO param);

}
