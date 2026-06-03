package com.example.hxds.bff.customer.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@Schema(description = "发送短信验证码表单")
public class SendSmsCodeForm {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[0-9]{10}$", message = "手机号格式不正确")
    @Schema(description = "新手机号")
    private String tel;
}
