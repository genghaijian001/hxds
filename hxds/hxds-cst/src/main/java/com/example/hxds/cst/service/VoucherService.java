package com.example.hxds.cst.service;

import java.util.HashMap;
import java.util.Map;

public interface VoucherService {

    long searchUnUseVoucherCount(long customerId);

    HashMap searchUnUseVoucherByPage(Map param);

    HashMap searchUsedVoucherByPage(Map param);

    HashMap searchUnTakeVoucherByPage(Map param);

    int takeVoucher(Map param);
}
