package com.example.hxds.odr.db.dao;

import com.example.hxds.odr.db.pojo.OrderBillEntity;

import java.util.HashMap;
import java.util.Map;

public interface OrderBillDao {
    public int insert(OrderBillEntity entity);
    //删除账单
    public int deleteUnAcceptOrderBill(long orderId);

    //更新订单、账单和分账记录
    public int updateBillFee(Map param);

    //小程序查询账单页面信息
    public HashMap searchReviewDriverOrderBill(Map param);

    //更新账单表实际支付
    public int updateBillPayment(Map param);
}




