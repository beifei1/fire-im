package com.fire.im.route.api.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @Author: wangzc
 * @Date: 2020/12/4 9:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户注册请求体")
public class RegisterDTO {

    @NotBlank(message = "登录账号不能为空")
    @ApiModelProperty("登录账号")
    private String account;

    @NotBlank(message = "昵称不能为空")
    @ApiModelProperty("昵称")
    private String nickName;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty("密码")
    private String password;

    @NotBlank(message = "头像不能为空")
    @ApiModelProperty("头像")
    private String avatar;

}
