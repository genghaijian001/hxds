package com.example.hxds.cst.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "删除客户车辆的表单")
public class DeleteCustomerCarByIdForm {
    @NotNull(message = "id不能为空")
    @Min(value = 1, message = "id不能小于1")
    @Schema(description = "车辆ID")
    private Long id;
}
