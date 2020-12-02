package com.fire.im.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: wangzc
 * @Date: 2020/11/24 11:24
 */

@Data
@ToString
@NoArgsConstructor
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String OK = "ok";
    private static final String FAIL = "fail";

    private Integer success;

    private String message;

    private T data;

    public Response(Integer success, String message,T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static Response success() {
        return new Response(1, OK,null);
    }

    public static Response success(Object data) {
        return new Response(1, OK, data);
    }

    public static Response failure() {
        return new Response(0,FAIL,null);
    }

    public static Response failure(String message) {
        return  new Response(0, message,null);
    }

}
