package com.example.hxds.rule.controller;

import com.example.hxds.common.util.R;
import com.example.hxds.rule.bean.Voucher;
import com.example.hxds.rule.controller.form.CalculateIncentiveFeeForm;
import com.example.hxds.rule.service.AwardRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/award"})
@Tag(name = "AwardRuleController", description = "系统奖励规则Web接口")
public class AwardRuleController   {
    @Resource
    private AwardRuleService awardRuleService;

    @PostMapping({"/calculateIncentiveFee"})
    @Operation(summary = "计算系统奖励")
    public R calculateIncentiveFee(@RequestBody @Valid CalculateIncentiveFeeForm a) {
        String str1 = this.awardRuleService.calculateIncentiveFee(a.getDriverId(), a.getAcceptTime());
        return R.ok().put(Voucher.ALLATORIxDEMO("\0359\034)\003("), str1);
    }
}

