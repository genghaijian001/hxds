package com.example.hxds.rule.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "计算订单分账的表单")
@Data
public class CalculateProfitsharingForm {
    
    @NotNull(message = "orderId不能为空")
    @Min(value = 1L, message = "orderId不能小于1")
    @Schema(description = "订单ID")
    private Long orderId;
    
    @NotBlank(message = "amount不能为空")
    @Pattern(regexp = "^[1-9]\\d*\\.\\d{1,2}$|^0\\.\\d{1,2}$|^[1-9]\\d*$", message = "amount内容不正确")
    @Schema(description = "待分账费用")
    private String amount;
}

  