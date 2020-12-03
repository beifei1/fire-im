package com.fire.im.route.api.pojo.vo;

import lombok.*;

import java.io.Serializable;

/**
 * @Author: wangzc
 * @Date: 2020/12/3 13:44
 */
@Data
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class RegisterVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String account;

    private String nickName;

    private String password;

}
