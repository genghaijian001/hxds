package com.example.hxds.rule.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "预估代驾费用的表单")
@Data
public class EstimateOrderChargeForm {
    @NotBlank(message = "mileage不能为空")
    @Pattern(regexp = "^[1-9]\\d*\\.\\d+$|^0\\.\\d*[1-9]\\d*$|^[1-9]\\d*$", message = "mileage内容不正确")

    @Schema(description = "代驾公里数")
    private String mileage;
    @NotBlank(message = "time不能为空")
    @Pattern(regexp = "^(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$", message = "time内容不正确")

    @Schema(description = "代驾开始时间")
    private String time;
}
   