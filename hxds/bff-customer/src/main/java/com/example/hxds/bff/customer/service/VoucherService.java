package com.example.hxds.bff.customer.service;

import com.example.hxds.bff.customer.controller.form.SearchUnTakeVoucherByPageForm;
import com.example.hxds.bff.customer.controller.form.SearchUnUseVoucherByPageForm;
import com.example.hxds.bff.customer.controller.form.SearchUsedVoucherByPageForm;
import com.example.hxds.bff.customer.controller.form.TakeVoucherForm;

import java.util.HashMap;

public interface VoucherService {

    long searchUnUseVoucherCount();

    HashMap searchUnUseVoucherByPage(SearchUnUseVoucherByPageForm form);

    HashMap searchUsedVoucherByPage(SearchUsedVoucherByPageForm form);

    HashMap searchUnTakeVoucherByPage(SearchUnTakeVoucherByPageForm form);

    int takeVoucher(TakeVoucherForm form);
}
