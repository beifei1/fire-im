package com.fire.im.route.api.pojo.vo;

import lombok.*;

import java.io.Serializable;

/**
 * @Author: wangzc
 * @Date: 2020/12/4 9:57
 */
@Data
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String nickName;

    private String avatar;

}
