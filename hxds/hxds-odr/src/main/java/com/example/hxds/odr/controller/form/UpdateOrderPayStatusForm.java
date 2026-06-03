package com.example.hxds.odr.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
@Schema(description = "司机端手动确认支付后更新订单支付状态的表单")
public class UpdateOrderPayStatusForm {

    @NotBlank(message = "uuid不能为空")
    @Schema(description = "订单UUID（微信支付out_trade_no）")
    private String uuid;

    @NotBlank(message = "transactionId不能为空")
    @Schema(description = "微信支付transactionId")
    private String transactionId;

    @NotBlank(message = "realPay不能为空")
    @Schema(description = "实际支付金额")
    private String realPay;
}
