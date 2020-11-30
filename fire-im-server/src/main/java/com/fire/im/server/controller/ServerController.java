package com.fire.im.server.controller;

import com.fire.im.common.exceptions.IMException;
import com.fire.im.common.pojo.Response;
import com.fire.im.server.api.ServerAPI;
import com.fire.im.server.api.pojo.PushMessageDTO;
import com.fire.im.server.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * @Author: wangzc
 * @Date: 2020/11/24 15:47
 */

@RestController
@RequestMapping("/")
public class ServerController implements ServerAPI {

    @Autowired
    private Server server;

    @Override
    @PostMapping(value = "pushMsg")
    public Response pushMessage(URI uri,@RequestBody PushMessageDTO message) {
        try {
            server.pushMessage(message);
        } catch (IMException e) {
            return Response.failure(e.getMessage());
        }

        return Response.success();
    }
}
