package com.example.hxds.bff.customer.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@Schema(description = "更新客户手机号表单")
public class UpdateCustomerTelForm {

    @Schema(description = "客户ID（由服务端从Token注入）")
    private Long customerId;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[0-9]{10}$", message = "手机号格式不正确")
    @Schema(description = "新手机号")
    private String tel;

    @NotBlank(message = "验证码不能为空")
    @Schema(description = "短信验证码")
    private String smsCode;
}
