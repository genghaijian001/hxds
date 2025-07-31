package com.example.hxds.bff.customer.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "查询客户车辆的表单")
public class SearchCustomerCarListForm {

    @Schema(description = "客户ID")
    private Long customerId;

}