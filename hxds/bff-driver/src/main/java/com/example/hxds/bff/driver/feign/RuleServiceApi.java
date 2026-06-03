package com.example.hxds.bff.driver.feign;

import com.example.hxds.bff.driver.controller.form.CalculateIncentiveFeeForm;
import com.example.hxds.bff.driver.controller.form.CalculateOrderChargeForm;
import com.example.hxds.bff.driver.controller.form.CalculateProfitsharingForm;
import com.example.hxds.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "hxds-rule")
public interface RuleServiceApi {
    /**
     * 计算代驾费
     * @param form
     * @return
     */
    @PostMapping("/charge/calculateOrderCharge")
    public R calculateOrderCharge(@RequestBody CalculateOrderChargeForm form);

    /**
     * 计算奖励费
     * @param form
     * @return
     */
    @PostMapping("/award/calculateIncentiveFee")
    public R calculateIncentiveFee(@RequestBody CalculateIncentiveFeeForm form);

    /**
     * 计算分账费
     * @param form
     * @return
     */
    @PostMapping("/profitsharing/calculateProfitsharing")
    public R calculateProfitsharing(@RequestBody CalculateProfitsharingForm form);
}