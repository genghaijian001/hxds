
package com.example.hxds.rule.controller;

import cn.hutool.core.map.MapUtil;
import com.example.hxds.common.util.R;
import com.example.hxds.rule.bean.Voucher;
import com.example.hxds.rule.controller.form.CalculateOrderChargeForm;
import com.example.hxds.rule.controller.form.EstimateOrderChargeForm;
import com.example.hxds.rule.controller.form.SearchChargeRuleByIdForm;
import com.example.hxds.rule.service.ChargeRuleService;
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
@RequestMapping({"/charge"})
@Tag(name = "ChargeRuleController", description = "代驾费用的Web接口")
public class ChargeRuleController   {


    @PostMapping({"/searchChargeRuleById"})
    @Operation(summary = "根据ID查询费用规则")
    public R searchChargeRuleById(@RequestBody @Valid SearchChargeRuleByIdForm a) {
        HashMap hashMap =new HashMap();
        return R.ok().put(Voucher.ALLATORIxDEMO("\0359\034)\003("), hashMap);
    }



    @PostMapping({"/calculateOrderCharge"})
    @Operation(summary = "计算代驾费用")
    public R calculateOrderCharge(@RequestBody @Valid CalculateOrderChargeForm a) {
        HashMap hashMap =new HashMap();
        hashMap.put("mileageFee","0.00");
        hashMap.put("returnFee","0.00");
        hashMap.put("waitingFee","0.00");
        hashMap.put("amount","1.0");
        hashMap.put("returnMileage","0.00");
        return R.ok().put(Voucher.ALLATORIxDEMO("\0359\034)\003("), hashMap);
    }

    @PostMapping({"/estimateOrderCharge"})
    public R estimateOrderCharge(@RequestBody @Valid EstimateOrderChargeForm a) {
        HashMap hashMap = new HashMap();
        hashMap.put("amount","1.00");
        hashMap.put("chargeRuleId","714601916034166785");
        hashMap.put("baseMileage",8);
        hashMap.put("baseMileagePrice","85");
        hashMap.put("exceedMileagePrice","3.5");
        hashMap.put("baseMinute",10);
        hashMap.put("exceedMinutePrice","1.0");
        hashMap.put("baseReturnMileage",8);
        hashMap.put("exceedReturnPrice","1.0");
        return R.ok().put(Voucher.ALLATORIxDEMO("\0359\034)\003("), hashMap);
    }

}
