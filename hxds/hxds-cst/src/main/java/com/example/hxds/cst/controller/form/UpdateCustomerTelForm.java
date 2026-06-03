package com.example.hxds.cst.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Data
@Schema(description = "更新客户手机号表单")
public class UpdateCustomerTelForm {
    @NotNull
    private Long customerId;

    @NotBlank
    @Pattern(regexp = "^1[0-9]{10}$", message = "手机号格式不正确")
    private String tel;
}
