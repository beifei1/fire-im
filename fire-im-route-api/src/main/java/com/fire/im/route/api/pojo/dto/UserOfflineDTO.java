package com.fire.im.route.api.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author: wangzc
 * @Date: 2020/11/26 15:49
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户下线请求体")
public class UserOfflineDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @ApiModelProperty(value = "userId", required = true)
    private String userId;
}
