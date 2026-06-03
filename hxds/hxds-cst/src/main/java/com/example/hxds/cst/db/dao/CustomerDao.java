package com.example.hxds.cst.db.dao;

import java.util.HashMap;
import java.util.Map;

public interface CustomerDao {
    int registerNewCustomer(Map param);

    long hasCustomer(Map param);

    String searchCustomerId(String openId);

    String searchCustomerIdByTel(String tel);

    String login(String openId);

    HashMap searchCustomerInfoInOrder(long customerId);

    HashMap searchCustomerBriefInfo(long customerId);

    String searchCustomerOpenId(long customerId);

    HashMap searchCustomerProfile(long customerId);

    int updateCustomerProfile(Map param);

    int updateCustomerPhoto(Map param);

    int updateCustomerTel(Map param);
}
