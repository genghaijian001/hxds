/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.example.hxds.rule.controller.form.CalculateDriverCancelOrderForm
 *  io.swagger.v3.oas.annotations.media.Schema
 *  javax.validation.constraints.Min
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.NotNull
 *  javax.validation.constraints.Pattern
 */
package com.example.hxds.rule.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "计算司机取消订单的表单")
@Data
public class CalculateDriverCancelOrderForm {
    
    @NotNull(message = "waitingMinute不能为空")
    @Min(value = 0L, message = "waitingMinute不能小于0")
    @Schema(description = "司机等时分钟数")
    private Short waitingMinute;
    
    @NotNull(message = "cancelNum不能为空")
    @Min(value = 0L, message = "cancelNum不能小于0")
    @Schema(description = "司机当天取消订单次数")
    private Short cancelNum;
    
    @NotNull(message = "acceptMinute不能为空")
    @Min(value = 0L, message = "acceptMinute不能小于0")
    @Schema(description = "接单口到当前的分钟数")
    private Short acceptMinute;
    
    @NotBlank(message = "status不能为空")
    @Pattern(regexp = "^未到达代驾点$||^到达代驾点$||^开始代驾$")
    @Schema(description = "状态")
    private String status;


}
