package com.fire.im.route.api.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @Author: wangzc
 * @Date: 2020/11/26 9:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("群发广播请求体")
public class BroadcastRequestDTO {

    @NotBlank
    @ApiModelProperty(value = "token", required = true)
    private String token;

    @NotBlank
    @ApiModelProperty(value = "msg", required = true)
    private String msg;

}
