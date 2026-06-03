package com.example.hxds.bff.customer.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import com.example.hxds.bff.customer.controller.form.SearchUnTakeVoucherByPageForm;
import com.example.hxds.bff.customer.controller.form.SearchUnUseVoucherByPageForm;
import com.example.hxds.bff.customer.controller.form.SearchUnUseVoucherCountForm;
import com.example.hxds.bff.customer.controller.form.SearchUsedVoucherByPageForm;
import com.example.hxds.bff.customer.controller.form.TakeVoucherForm;
import com.example.hxds.bff.customer.feign.CstServiceApi;
import com.example.hxds.bff.customer.service.VoucherService;
import com.example.hxds.common.util.R;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.HashMap;

@Service
public class VoucherServiceImpl implements VoucherService {

    @Resource
    private CstServiceApi cstServiceApi;

    @Override
    public long searchUnUseVoucherCount() {
        long customerId = StpUtil.getLoginIdAsLong();
        SearchUnUseVoucherCountForm form = new SearchUnUseVoucherCountForm();
        form.setCustomerId(customerId);
        R r = cstServiceApi.searchUnUseVoucherCount(form);
        return Convert.toLong(r.get("result"));
    }

    @Override
    public HashMap searchUnUseVoucherByPage(SearchUnUseVoucherByPageForm form) {
        long customerId = StpUtil.getLoginIdAsLong();
        form.setCustomerId(customerId);
        R r = cstServiceApi.searchUnUseVoucherByPage(form);
        return (HashMap) r.get("result");
    }

    @Override
    public HashMap searchUsedVoucherByPage(SearchUsedVoucherByPageForm form) {
        long customerId = StpUtil.getLoginIdAsLong();
        form.setCustomerId(customerId);
        R r = cstServiceApi.searchUsedVoucherByPage(form);
        return (HashMap) r.get("result");
    }

    @Override
    public HashMap searchUnTakeVoucherByPage(SearchUnTakeVoucherByPageForm form) {
        long customerId = StpUtil.getLoginIdAsLong();
        form.setCustomerId(customerId);
        R r = cstServiceApi.searchUnTakeVoucherByPage(form);
        return (HashMap) r.get("result");
    }

    @Override
    public int takeVoucher(TakeVoucherForm form) {
        long customerId = StpUtil.getLoginIdAsLong();
        form.setCustomerId(customerId);
        R r = cstServiceApi.takeVoucher(form);
        return Convert.toInt(r.get("rows"));
    }
}
