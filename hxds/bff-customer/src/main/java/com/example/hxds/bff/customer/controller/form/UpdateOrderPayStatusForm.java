package com.example.hxds.bff.customer.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "调用hxds-odr更新订单支付状态的表单")
public class UpdateOrderPayStatusForm {

    @Schema(description = "订单UUID（微信支付out_trade_no）")
    private String uuid;

    @Schema(description = "微信支付transactionId")
    private String transactionId;

    @Schema(description = "实际支付金额")
    private String realPay;
}
