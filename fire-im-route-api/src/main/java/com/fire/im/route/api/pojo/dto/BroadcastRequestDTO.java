package com.fire.im.route.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * @Author: wangzc
 * @Date: 2020/11/26 9:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "群发广播请求体")
public class BroadcastRequestDTO {

    @NotBlank
    @Schema(description = "token", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;

    @NotBlank
    @Schema(description = "msg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String msg;

}
