package com.example.hxds.cst.service.impl;

import cn.hutool.core.map.MapUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.common.util.MicroAppUtil;
import com.example.hxds.cst.db.dao.CustomerDao;
import com.example.hxds.cst.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerDao customerDao;

    @Resource
    private MicroAppUtil microAppUtil;

    /**
     * 注册新用户
     * @param param
     * @return
     */
    @Override
    @Transactional
    @LcnTransaction
    public String registerNewCustomer(Map param) {
        String code = MapUtil.getStr(param, "code");
        String openId = microAppUtil.getOpenId(code);
        HashMap tempParam = new HashMap() {{
            put("openId", openId);
        }};
        if (customerDao.hasCustomer(tempParam) != 0) {
            throw new HxdsException("该微信无法注册");
        }
        param.put("openId", openId);
        customerDao.registerNewCustomer(param);
        String customerId = customerDao.searchCustomerId(openId);
        return customerId;
    }

    @Override
    public String login(String code) {
        String openId = microAppUtil.getOpenId(code);
        String customerId = customerDao.login(openId);
        customerId=(customerId != null ? customerId : "");
        return customerId;
    }

    /**
     * 加载执行订单，查询乘客信息
     * @param customerId
     * @return
     */
    @Override
    public HashMap searchCustomerInfoInOrder(long customerId) {
        HashMap map = customerDao.searchCustomerInfoInOrder(customerId);
        return map;
    }

    /**
     * mis查询乘客信息  折叠面板
     * @param customerId
     * @return
     */
    @Override
    public HashMap searchCustomerBriefInfo(long customerId) {
        HashMap map = customerDao.searchCustomerBriefInfo(customerId);
        return map;
    }

    @Override
    public String searchCustomerOpenId(long customerId) {
        String openId = customerDao.searchCustomerOpenId(customerId);
        return openId;
    }
}
