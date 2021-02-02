package com.fire.im.common.pojo.dto;

import lombok.*;

/**
 * server实例信息
 * @Author: wangzc
 * @Date: 2020/11/25 15:51
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Server {

    private String ip;

    private Integer httpPort;

    private Integer socketPort;

}
