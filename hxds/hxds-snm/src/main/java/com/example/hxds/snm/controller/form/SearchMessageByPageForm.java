package com.example.hxds.snm.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "分页查询用户消息的表单")
public class SearchMessageByPageForm {
    @NotNull(message = "userId不能为空")
    @Min(value = 1, message = "userId不能小于1")
    @Schema(description = "用户ID")
    private Long userId;

    @NotBlank(message = "identity不能为空")
    @Schema(description = "用户身份")
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
