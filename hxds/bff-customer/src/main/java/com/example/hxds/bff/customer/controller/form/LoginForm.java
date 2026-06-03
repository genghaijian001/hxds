package com.example.hxds.bff.customer.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@Schema(description = "Customer login form")
public class LoginForm {
    @NotBlank(message = "code cannot be blank")
    @Schema(description = "WeChat login code")
    private String code;

    @Pattern(regexp = "^$|^1[0-9]{10}$", message = "tel format is invalid")
    @Schema(description = "Mobile number used for SMS login/register")
    private String tel;

    @Pattern(regexp = "^$|^[0-9]{6}$", message = "smsCode format is invalid")
    @Schema(description = "SMS verification code")
    private String smsCode;
}
