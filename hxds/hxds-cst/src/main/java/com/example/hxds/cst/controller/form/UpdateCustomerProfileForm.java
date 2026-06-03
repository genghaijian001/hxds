package com.example.hxds.cst.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Data
@Schema(description = "更新客户个人资料表单")
public class UpdateCustomerProfileForm {

    @NotNull(message = "customerId不能为空")
    @Schema(description = "客户ID")
    private Long customerId;

    @NotBlank(message = "昵称不能为空")
    @Length(min = 1, max = 20, message = "昵称长度1-20字")
    @Schema(description = "昵称")
    private String nickname;

    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "^(男|女|保密)$", message = "性别值不合法")
    @Schema(description = "性别：男/女/保密")
    private String sex;
}
