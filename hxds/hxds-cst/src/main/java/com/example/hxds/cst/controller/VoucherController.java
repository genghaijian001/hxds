package com.example.hxds.cst.controller;

import cn.hutool.core.bean.BeanUtil;
import com.example.hxds.common.util.R;
import com.example.hxds.cst.controller.form.SearchCustomerBriefInfoForm;
import com.example.hxds.cst.controller.form.SearchVoucherByPageForm;
import com.example.hxds.cst.controller.form.TakeVoucherForm;
import com.example.hxds.cst.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/voucher")
@Tag(name = "VoucherController", description = "代金券Web接口")
public class VoucherController {

    @Resource
    private VoucherService voucherService;

    @PostMapping("/searchUnUseVoucherCount")
    @Operation(summary = "查询未使用代金券数量")
    public R searchUnUseVoucherCount(@RequestBody @Valid SearchCustomerBriefInfoForm form) {
        long count = voucherService.searchUnUseVoucherCount(form.getCustomerId());
        return R.ok().put("result", count);
    }

    @PostMapping("/searchUnUseVoucherByPage")
    @Operation(summary = "查询未使用代金券分页")
    public R searchUnUseVoucherByPage(@RequestBody @Valid SearchVoucherByPageForm form) {
        Map param = BeanUtil.beanToMap(form);
        HashMap map = voucherService.searchUnUseVoucherByPage(param);
        return R.ok().put("result", map);
    }

    @PostMapping("/searchUsedVoucherByPage")
    @Operation(summary = "查询已使用代金券分页")
    public R searchUsedVoucherByPage(@RequestBody @Valid SearchVoucherByPageForm form) {
        Map param = BeanUtil.beanToMap(form);
        HashMap map = voucherService.searchUsedVoucherByPage(param);
        return R.ok().put("result", map);
    }

    @PostMapping("/searchUnTakeVoucherByPage")
    @Operation(summary = "查询未领取代金券分页")
    public R searchUnTakeVoucherByPage(@RequestBody @Valid SearchVoucherByPageForm form) {
        Map param = BeanUtil.beanToMap(form);
        HashMap map = voucherService.searchUnTakeVoucherByPage(param);
        return R.ok().put("result", map);
    }

    @PostMapping("/takeVoucher")
    @Operation(summary = "领取代金券")
    public R takeVoucher(@RequestBody @Valid TakeVoucherForm form) {
        Map param = BeanUtil.beanToMap(form);
        int rows = voucherService.takeVoucher(param);
        return R.ok().put("rows", rows);
    }
}
