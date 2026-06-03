package com.example.hxds.cst.db.dao;

import java.util.ArrayList;
import java.util.Map;

public interface VoucherDao {

    long searchUnUseVoucherCount(long customerId);

    ArrayList searchUnUseVoucherByPage(Map param);

    long searchUnUseVoucherByPageCount(Map param);

    ArrayList searchUsedVoucherByPage(Map param);

    long searchUsedVoucherByPageCount(Map param);

    ArrayList searchUnTakeVoucherByPage(Map param);

    long searchUnTakeVoucherByPageCount(Map param);

    int takeVoucher(Map param);
}
