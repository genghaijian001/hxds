package com.example.hxds.bff.customer.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
@Schema(description = "根据ID查询消息的表单")
public class SearchMessageByIdForm {
    @NotBlank(message = "id不能为空")
    @Schema(description = "消息Ref的ID")
    private String id;
}
