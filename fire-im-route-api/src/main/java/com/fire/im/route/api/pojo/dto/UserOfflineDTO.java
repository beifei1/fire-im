package com.fire.im.route.api.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: wangzc
 * @Date: 2020/11/26 15:49
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户下线请求体")
public class UserOfflineDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Schema(description = "userId", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> userIds;
}
