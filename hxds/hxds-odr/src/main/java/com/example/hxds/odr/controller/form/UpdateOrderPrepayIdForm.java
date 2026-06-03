package com.example.hxds.odr.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "更新预支付订单ID的表单")
public class UpdateOrderPrepayIdForm {
    @NotBlank(message = "prepayId不能为空")
    @Schema(description = "预支付订单ID")
    private String prepayId;

    @NotNull(message = "orderId不能为空")
    @Min(value = 1, message = "orderId不能小于1")
    @Schema(description = "订单ID")
    private Long orderId;
}
