package com.fire.im.route.web;

import com.fire.im.common.exceptions.IMException;
import com.fire.im.common.pojo.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: wangzc
 * @Date: 2020/11/25 16:53
 */

@RestControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({IMException.class})
    public Response ImExceptionHandler(IMException e) {
        return Response.failure(e.getMessage());
    }

}
