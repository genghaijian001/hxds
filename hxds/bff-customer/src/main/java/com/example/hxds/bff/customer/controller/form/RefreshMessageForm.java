package com.example.hxds.bff.customer.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "刷新消息的表单")
public class RefreshMessageForm {
    @Schema(description = "用户ID(从Token注入)")
    private Long userId;

    @Schema(description = "用户身份")
    private String identity;
}
