package com.example.hxds.snm.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Data
@Schema(description = "刷新用户消息的表单")
public class RefreshMessageForm {
    @NotNull(message = "userId不能为空")
    @Min(value = 1, message = "userId不能小于1")
    @Schema(description = "用户ID")
    private Long userId;

    @NotBlank(message = "identity不能为空")
    @Pattern(regexp = "^driver$|^mis$|^customer$",message = "identity内容不正确")
    @Schema(description = "用户身份")
    private String identity;

}