package com.fire.im.route.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * @Author: wangzc
 * @Date: 2020/11/25 15:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户登录请求体")
public class UserLoginDTO {

    @NotBlank
    @Schema(description = "account", requiredMode = Schema.RequiredMode.REQUIRED)
    private String account;

    @NotBlank
    @Schema(description = "password", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

}
