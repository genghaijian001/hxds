package com.example.hxds.rule.controller;

import cn.hutool.core.map.MapUtil;
import com.example.hxds.common.util.R;
import com.example.hxds.rule.controller.form.CalculateProfitsharingForm;
import com.example.hxds.rule.controller.form.SearchChargeRuleByIdForm;
import com.example.hxds.rule.controller.form.SearchProfitsharingRuleByIdForm;
import com.example.hxds.rule.service.ProfitsharingRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping({"/profitsharing"})
@Tag(name = "ProfitsharingRuleController", description = "分账规则Web接口")
public class ProfitsharingRuleController {

    @Resource
    private ProfitsharingRuleService profitsharingRuleService;


    @PostMapping({"/searchProfitsharingRuleById"})
    @Operation(summary = "根据ID查询分账规则")
    public R searchProfitsharingRuleById(@RequestBody @Valid SearchProfitsharingRuleByIdForm a) {
        HashMap hashMap = this.profitsharingRuleService.searchProfitsharingRuleById(a.getRuleId().longValue());
        return R.ok().put(SearchChargeRuleByIdForm.ALLATORIxDEMO("fXgHxI"), hashMap);
    }


    @PostMapping({"/calculateProfitsharing"})
    @Operation(summary = "计算分账规则")
    public R calculateProfitsharing(@RequestBody @Valid CalculateProfitsharingForm a) {
        HashMap hashMap = new HashMap();
        hashMap.put("ruleId", 1111L);
        hashMap.put("systemIncome", "0.00");
        hashMap.put("driverIncome","1.00");
        hashMap.put("paymentRate", "0.78");
        hashMap.put("paymentFee", "0.00");
        hashMap.put("taxRate", "0.00");
        hashMap.put("taxFee", "0.00");
        return R.ok().put(SearchChargeRuleByIdForm.ALLATORIxDEMO("fXgHxI"), hashMap);
    }
}
