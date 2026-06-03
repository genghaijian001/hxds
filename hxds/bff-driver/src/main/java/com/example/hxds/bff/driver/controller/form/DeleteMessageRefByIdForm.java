package com.example.hxds.bff.driver.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
@Schema(description = "删除消息的表单")
public class DeleteMessageRefByIdForm {
    @NotBlank(message = "id不能为空")
    @Schema(description = "消息Ref的ID")
    private String id;
}
