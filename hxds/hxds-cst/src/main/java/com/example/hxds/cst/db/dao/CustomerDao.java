package com.example.hxds.cst.db.dao;

import java.util.HashMap;
import java.util.Map;

public interface CustomerDao {
    //注册新用户
    public int registerNewCustomer(Map param);
    //根据openId或者customerId查询是否存在用户记录
    public long hasCustomer(Map param);
    //根据openId查询插入记录的主键值。
    public String searchCustomerId(String openId);

    public String login(String openId);
    //加载执行订单，查询乘客信息
    public HashMap searchCustomerInfoInOrder( long customerId);
    //mis查询乘客信息  折叠面板
    public HashMap searchCustomerBriefInfo(long customerId);

    //
    public String searchCustomerOpenId(long customerId);
}
