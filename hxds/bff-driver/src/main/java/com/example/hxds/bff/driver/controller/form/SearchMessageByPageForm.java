package com.example.hxds.bff.driver.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "司机端分页查询消息的表单")
public class SearchMessageByPageForm {
    @Schema(description = "用户ID - 由后端从SaToken自动填充")
    private Long userId;

    @Schema(description = "身份标识 - 由后端自动填充为driver")
    private String identity;

    @NotNull(message = "page不能为空")
    @Min(value = 1, message = "page不能小于1")
    @Schema(description = "页码")
    private Long page;

    @NotNull(message = "length不能为空")
    @Min(value = 1, message = "length不能小于1")
    @Schema(description = "每页记录数")
    private Long length;
}
