package com.example.hxds.dr.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "查询司机钱包的表单")
public class SearchDriverWalletForm {

    @NotNull(message = "driverId不能为空")
    @Min(value = 1, message = "driverId不能小于1")
    @Schema(description = "司机ID")
    private Long driverId;

    @NotBlank(message = "month不能为空")
    @Schema(description = "查询月份，格式yyyy-MM")
    private String month;
}
