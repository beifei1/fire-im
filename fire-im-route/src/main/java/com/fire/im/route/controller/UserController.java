package com.fire.im.route.controller;

import com.fire.im.common.exceptions.IMException;
import com.fire.im.common.pojo.Response;
import com.fire.im.common.pojo.Void;
import com.fire.im.common.pojo.dto.Server;
import com.fire.im.common.route.Router;
import com.fire.im.common.utils.SnowflakeId;
import com.fire.im.route.api.pojo.dto.RegisterDTO;
import com.fire.im.route.api.pojo.dto.UserLoginDTO;
import com.fire.im.route.api.pojo.dto.UserOfflineDTO;
import com.fire.im.route.api.pojo.vo.UserVO;
import com.fire.im.route.api.pojo.vo.LoginVO;
import com.fire.im.route.config.AppGlobalConfig;
import com.fire.im.route.service.IRouteService;
import com.fire.im.route.utils.RedisUtil;
import com.fire.im.route.utils.RouterUtil;
import com.fire.im.route.utils.ServerHolder;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;

/**
 * @Author: wangzc
 * @Date: 2020/11/25 15:32
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController {

//    //单机部署测试用
//    static AtomicInteger userIdProducer = new AtomicInteger(0);

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

    @Autowired
    SnowflakeId snowflake;

    /**
     * 用户登录
     * @param param
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Response<LoginVO> login(@Valid @RequestBody UserLoginDTO param) {

        if (!redisUtil.hasKey(RouterUtil.Consts.Profile.USER_PROFILE_PREFIX + param.getAccount())) {
            throw new IMException("account or password not exists!");
        }

        String password = DigestUtils.md5Hex(param.getPassword());
        String dbPassword = (String)redisUtil.hget(RouterUtil.Consts.Profile.USER_PROFILE_PREFIX + param.getAccount(), RouterUtil.Consts.Profile.USER_PASSWORD_PARAM);
        if (!StringUtils.equalsIgnoreCase(password, dbPassword)) {
            throw new IMException("account or password not exists!");
        }

        String userId = (String)redisUtil.hget(RouterUtil.Consts.Profile.USER_PROFILE_PREFIX + param.getAccount(), RouterUtil.Consts.Profile.USER_ID_PARAM);
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
        routeService.saveRouteInfo(userId, serverStr);

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
     * 用户注册
     * @param param
     * @return
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Response<Void> register(@Valid @RequestBody RegisterDTO param) {
       if (redisUtil.hasKey(RouterUtil.Consts.Profile.USER_PROFILE_PREFIX + param.getAccount())) {
           throw new IMException("account already exists!");
       }
       //密码
        redisUtil.hset(
                RouterUtil.Consts.Profile.USER_PROFILE_PREFIX + param.getAccount(),
                RouterUtil.Consts.Profile.USER_PASSWORD_PARAM, DigestUtils.md5Hex(param.getPassword()));
       //头像
        redisUtil.hset(
                RouterUtil.Consts.Profile.USER_PROFILE_PREFIX + param.getAccount(),
                RouterUtil.Consts.Profile.USER_NICKNAME_PARAM, StringUtils.trimToNull(param.getNickName()));
        //昵称
        redisUtil.hset(
                RouterUtil.Consts.Profile.USER_PROFILE_PREFIX + param.getAccount(),
                RouterUtil.Consts.Profile.USER_AVATAR_PARAM, StringUtils.trimToNull(param.getAvatar()));
        //userId
        redisUtil.hset(
                RouterUtil.Consts.Profile.USER_PROFILE_PREFIX + param.getAccount(),
                RouterUtil.Consts.Profile.USER_ID_PARAM, String.valueOf(snowflake.nextId()));

        return Response.success();
    }

    /**
     * 用户下线，清除路由关系
     * @param param
     * @return
     */
    @PostMapping("/offline")
    @ApiOperation("用户下线")
    public Response<Void> offline(@Valid @RequestBody UserOfflineDTO param) {
        log.info("用户: {} offline", param.getUserIds().toString());
        routeService.userOffline(param);
        return Response.success();
    }


    /**
     * 所有联系人
     * @return
     */
    @PostMapping("/users")
    @ApiOperation("所有联系人")
    public Response<List<UserVO>> register() {
        //获取所有保存的路由信息
        Set<String> routes = redisUtil.keys(RouterUtil.Consts.Profile.USER_PROFILE_PREFIX + "*");
        List<UserVO> users = Lists.newArrayListWithCapacity(routes.size());
        routes.stream().forEach(key -> {
            String userId = (String)redisUtil.hget(key, RouterUtil.Consts.Profile.USER_ID_PARAM);
            String nickName = (String)redisUtil.hget(key,RouterUtil.Consts.Profile.USER_NICKNAME_PARAM);
            String avatar = (String)redisUtil.hget(key,RouterUtil.Consts.Profile.USER_AVATAR_PARAM);
            users.add(
                    UserVO.builder().userId(userId).nickName(nickName).avatar(avatar).build()
            );
        });

        return Response.success(users);
    }

}
