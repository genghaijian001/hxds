package com.example.hxds.bff.driver.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
@Schema(description = "查询司机钱包的表单")
public class SearchDriverWalletForm {

    @Schema(description = "司机ID")
    private Long driverId;

    @NotBlank(message = "month不能为空")
    @Schema(description = "查询月份，格式yyyy-MM")
    private String month;
}
