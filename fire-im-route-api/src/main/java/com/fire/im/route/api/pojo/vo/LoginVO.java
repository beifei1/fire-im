package com.fire.im.route.api.pojo.vo;

import lombok.*;

import java.io.Serializable;

/**
 * @Author: wangzc
 * @Date: 2020/11/25 15:44
 */
@Data
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class LoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int socketPort;

    private int httpPort;

    private String ip;

    private String token;

    private String userId;
}
