package com.example.hxds.odr.service;

import java.util.HashMap;
import java.util.Map;

public interface OrderBillService {
    //更新订单、账单和分账记录
    public int updateBillFee(Map param);

    //小程序查询账单页面信息
    public HashMap searchReviewDriverOrderBill(Map param);

    //更新账单表实际支付
    public int updateBillPayment(Map param);
}
