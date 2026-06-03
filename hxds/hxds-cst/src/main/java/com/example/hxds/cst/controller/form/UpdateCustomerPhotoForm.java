package com.example.hxds.cst.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "更新客户头像表单")
public class UpdateCustomerPhotoForm {

    @NotNull(message = "customerId不能为空")
    @Schema(description = "客户ID")
    private Long customerId;

    @NotBlank(message = "photo不能为空")
    @Schema(description = "头像URL")
    private String photo;
}
