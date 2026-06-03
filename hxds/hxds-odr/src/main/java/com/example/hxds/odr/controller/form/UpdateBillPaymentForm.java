package com.example.hxds.odr.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Data
@Schema(description = "更新账单实际支付费用的表单")
public class UpdateBillPaymentForm {
    @NotBlank(message = "realPay不能为空")
    @Pattern(regexp = "^[1-9]\\d*\\.\\d{1,2}$|^0\\.\\d{1,2}$|^[1-9]\\d*$", message = "realPay内容不正确")
    @Schema(description = "实际支付金额")
    private String realPay;

    @NotBlank(message = "voucherFee不能为空")
    @Pattern(regexp = "^[1-9]\\d*\\.\\d{1,2}$|^0\\.\\d{1,2}$|^[1-9]\\d*$", message = "voucherFee内容不正确")
    @Schema(description = "代金券面额")
    private String voucherFee;

    @NotNull(message = "orderId不能为空")
    @Min(value = 1, message = "orderId不能小于1")
    @Schema(description = "订单ID")
    private Long orderId;
}