package com.fire.im.server.api.pojo;

import lombok.*;

import java.io.Serializable;

/**
 * @Author: wangzc
 * @Date: 2020/11/24 11:29
 */

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PushMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;

    private String userId;

    private String fromUserId;

}
