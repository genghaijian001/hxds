package com.example.hxds.bff.driver.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "刷新消息的表单")
public class RefreshMessageForm {
    @Schema(description = "用户ID(从Token注入)")
    private Long userId;

    @Schema(description = "用户身份")
    private String identity;
}
