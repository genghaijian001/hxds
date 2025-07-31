package com.example.hxds.bff.customer.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "查询未使用代金券数量的表单")
public class SearchUnUseVoucherCountForm {

    @Schema(description = "乘客ID")
    private Long customerId;
}