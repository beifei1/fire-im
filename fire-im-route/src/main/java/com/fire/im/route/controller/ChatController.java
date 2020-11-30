package com.fire.im.route.controller;

import com.fire.im.common.pojo.Response;
import com.fire.im.common.pojo.Void;
import com.fire.im.route.api.pojo.dto.BroadcastRequestDTO;
import com.fire.im.route.api.pojo.dto.P2PChatRequestDTO;
import com.fire.im.route.service.IMessageService;
import com.fire.im.route.utils.RedisUtil;
import com.fire.im.route.utils.RouterUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: wangzc
 * @Date: 2020/11/25 15:31
 */
@Api(tags = "聊天接口")
@RequestMapping("/chat")
@RestController
public class ChatController {

    @Autowired
    private IMessageService messageService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 点对点聊天
     * @return
     */
    @PostMapping("/p2p")
    @ApiOperation("点对点聊天")
    public Response<Void> p2pChat(@Valid @RequestBody P2PChatRequestDTO param) {

        String userId = (String)redisUtil.hget(RouterUtil.Consts.USER_INFO_PREFIX + param.getToken(), RouterUtil.Consts.USER_ID_PARAMETER);
        messageService.sendMessage(param.getMsg(),userId,param.getToUserId());

        return Response.success();
    }


    /**
     * 广播
     * @return
     */
    @PostMapping("/broadcast")
    @ApiOperation("群发广播")
    public Response<Void> broadcast(@Valid @RequestBody BroadcastRequestDTO param) {
        String userId = (String)redisUtil.hget(RouterUtil.Consts.USER_INFO_PREFIX + param.getToken(), RouterUtil.Consts.USER_ID_PARAMETER);
        messageService.broadcastMessage(userId, param.getMsg());
        return Response.success();
    }

}
