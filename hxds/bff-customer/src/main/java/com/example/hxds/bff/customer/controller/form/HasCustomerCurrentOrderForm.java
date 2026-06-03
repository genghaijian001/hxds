package com.example.hxds.bff.customer.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "查询乘客是否存在当前的订单的表单")
public class HasCustomerCurrentOrderForm {

    @Schema(description = "客户ID")
    private Long customerId;
}
