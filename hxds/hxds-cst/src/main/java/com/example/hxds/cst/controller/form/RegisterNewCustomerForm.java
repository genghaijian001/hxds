package com.example.hxds.cst.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@Schema(description = "注册新客户")
public class RegisterNewCustomerForm {
    @NotBlank(message = "code不能为空")
    @Schema(description = "微信小程序临时授权")
    private String code;

    @NotBlank(message = "nickname不能为空")
    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String photo;

    @Pattern(regexp = "^男$|^女$|^无$|^$", message = "sex内容不正确")
    private String sex;

    @Pattern(regexp = "^1\\d{10}$|^$", message = "tel内容不正确")
    @Schema(description = "电话")
    private String tel;

}
