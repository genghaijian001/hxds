package com.example.hxds.cst.service;

import java.util.HashMap;
import java.util.Map;

public interface CustomerService {
    String registerNewCustomer(Map param);

    String login(Map param);

    HashMap searchCustomerInfoInOrder(long customerId);

    HashMap searchCustomerBriefInfo(long customerId);

    String searchCustomerOpenId(long customerId);

    HashMap searchCustomerProfile(long customerId);

    int updateCustomerProfile(Map param);

    int updateCustomerPhoto(Map param);

    int updateCustomerTel(Map param);
}
