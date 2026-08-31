package com.fire.im.route.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * @Author: wangzc
 * @Date: 2020/12/4 9:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户注册请求体")
public class RegisterDTO {

    @NotBlank(message = "登录账号不能为空")
    @Schema(description = "登录账号")
    private String account;

    @NotBlank(message = "昵称不能为空")
    @Schema(description = "昵称")
    private String nickName;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;

    @NotBlank(message = "头像不能为空")
    @Schema(description = "头像")
    private String avatar;

}
