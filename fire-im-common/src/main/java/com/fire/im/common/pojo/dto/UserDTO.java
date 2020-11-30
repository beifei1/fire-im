package com.fire.im.common.pojo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wangzc
 * @Date: 2020/11/24 10:29
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 手机号码
     */
    private String mobile;

}
