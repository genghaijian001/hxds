package com.example.hxds.rule.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


@Schema(description = "计算代驾费用的表单")
@Data
public class CalculateOrderChargeForm {
    @NotNull(message = "minute不能为空")
    @Min(value = 0L, message = "minute不能小于0")
    @Schema(description = "等时分钟")
    private Integer minute;
    @NotBlank(message = "time不能为空")
    @Pattern(regexp = "^(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$", message = "time内容不正确")
    @Schema(description = "代驾开始时间")
    private String time;
    @NotBlank(message = "mileage不能为空")
    @Pattern(regexp = "^[1-9]\\d*\\.\\d+$|^0\\.\\d+$|^[1-9]\\d*$", message = "mileage内容不正确")
    @Schema(description = "代驾公里数")
    private String mileage;
}
