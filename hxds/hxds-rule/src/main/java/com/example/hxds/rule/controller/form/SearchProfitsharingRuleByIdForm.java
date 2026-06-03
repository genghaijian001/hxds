package com.example.hxds.rule.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "根据ID查询分账规则的表单")
@Data
public class SearchProfitsharingRuleByIdForm {
    @NotNull(message = "ruleId不为空")
    @Min(value = 1L, message = "ruleId不能小于1")
    @Schema(description = "规则ID")
    private Long ruleId;
}