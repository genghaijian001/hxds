package com.example.hxds.bff.customer.controller;

import com.example.hxds.bff.customer.controller.form.SearchUnTakeVoucherByPageForm;
import com.example.hxds.bff.customer.controller.form.SearchUnUseVoucherByPageForm;
import com.example.hxds.bff.customer.controller.form.SearchUsedVoucherByPageForm;
import com.example.hxds.bff.customer.controller.form.TakeVoucherForm;
import com.example.hxds.bff.customer.service.VoucherService;
import com.example.hxds.common.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/voucher")
@Tag(name = "VoucherController", description = "代金券Web接口")
public class VoucherController {

    @Resource
    private VoucherService voucherService;

    @PostMapping("/searchUnUseVoucherCount")
    @Operation(summary = "查询未使用代金券数量")
    public R searchUnUseVoucherCount() {
        long count = voucherService.searchUnUseVoucherCount();
        return R.ok().put("result", count);
    }

    @PostMapping("/searchUnUseVoucherByPage")
    @Operation(summary = "查询未使用代金券分页")
    public R searchUnUseVoucherByPage(@RequestBody @Valid SearchUnUseVoucherByPageForm form) {
        HashMap map = voucherService.searchUnUseVoucherByPage(form);
        return R.ok().put("result", map);
    }

    @PostMapping("/searchUsedVoucherByPage")
    @Operation(summary = "查询已使用代金券分页")
    public R searchUsedVoucherByPage(@RequestBody @Valid SearchUsedVoucherByPageForm form) {
        HashMap map = voucherService.searchUsedVoucherByPage(form);
        return R.ok().put("result", map);
    }

    @PostMapping("/searchUnTakeVoucherByPage")
    @Operation(summary = "查询未领取代金券分页")
    public R searchUnTakeVoucherByPage(@RequestBody @Valid SearchUnTakeVoucherByPageForm form) {
        HashMap map = voucherService.searchUnTakeVoucherByPage(form);
        return R.ok().put("result", map);
    }

    @PostMapping("/takeVoucher")
    @Operation(summary = "领取代金券")
    public R takeVoucher(@RequestBody @Valid TakeVoucherForm form) {
        int rows = voucherService.takeVoucher(form);
        return R.ok().put("rows", rows);
    }
}
