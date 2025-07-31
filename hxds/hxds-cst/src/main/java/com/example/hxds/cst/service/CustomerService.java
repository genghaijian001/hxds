package com.example.hxds.cst.service;

import java.util.HashMap;
import java.util.Map;

public interface CustomerService {
    //注册新用户
    public String registerNewCustomer(Map param);
    public String login(String code);

    //加载执行订单，查询乘客信息
    public HashMap searchCustomerInfoInOrder(long customerId);
    //mis查询乘客信息  折叠面板
    public HashMap searchCustomerBriefInfo(long customerId);

    public String searchCustomerOpenId(long customerId);
}
