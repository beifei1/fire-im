package com.fire.im.route.controller;

import com.fire.im.common.pojo.Response;
import com.fire.im.common.pojo.Void;
import com.fire.im.common.pojo.dto.Server;
import com.fire.im.common.route.Router;
import com.fire.im.route.api.pojo.dto.UserLoginDTO;
import com.fire.im.route.api.pojo.dto.UserOfflineDTO;
import com.fire.im.route.api.pojo.vo.LoginVO;
import com.fire.im.route.config.AppGlobalConfig;
import com.fire.im.route.service.IRouteService;
import com.fire.im.route.utils.RedisUtil;
import com.fire.im.route.utils.RouterUtil;
import com.fire.im.route.utils.ServerHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: wangzc
 * @Date: 2020/11/25 15:32
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController {

    //单机部署测试用
    static AtomicInteger userIdProducer = new AtomicInteger(0);

    @Autowired
    Router router;

    @Autowired
    ServerHolder servers;

    @Autowired
    IRouteService routeService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    AppGlobalConfig appConfig;

    /**
     * 用户登录
     * @param param
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Response<LoginVO> login(@Valid @RequestBody UserLoginDTO param) {

        //TODO 伪实现
        String userId = String.valueOf(userIdProducer.incrementAndGet());
        //生成token
        String token = DigestUtils.md5Hex(userId + appConfig.getTokenSecret());
        //选择服务器
        String serverStr = router.chooseServer(servers.getAll(), userId);

        log.info("useraccount {} route server {}", param.getAccount(), serverStr);
        //parse server 信息
        Server server = RouterUtil.parse(serverStr);

        //检测server是否可达
        routeService.isServerAvailable(server);

        //保存路由信息
        routeService.saveRouteInfo(userId,serverStr);

        //保存用户信息
        redisUtil.hset(RouterUtil.Consts.USER_INFO_PREFIX + token, RouterUtil.Consts.USER_ID_PARAMETER, userId);
        redisUtil.hset(RouterUtil.Consts.USER_INFO_PREFIX + token, RouterUtil.Consts.USER_TOKEN_PARAMETER, token);

        //设置响应信息
        LoginVO resp = LoginVO.builder()
                .httpPort(server.getHttpPort())
                .socketPort(server.getSocketPort())
                .ip(server.getIp())
                .userId(userId)
                .token(token).build();

        return Response.success(resp);
    }

    /**
     * 用户下线，清除路由关系
     * @param param
     * @return
     */
    @PostMapping("/offline")
    @ApiOperation("用户下线")
    public Response<Void> offline(@Valid @RequestBody UserOfflineDTO param) {
        log.info("用户:{} offline");
        routeService.userOffline(param);
        return Response.success();
    }

}
