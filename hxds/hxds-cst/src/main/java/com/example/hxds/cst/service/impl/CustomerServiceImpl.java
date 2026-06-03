package com.example.hxds.cst.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.common.util.MicroAppUtil;
import com.example.hxds.cst.db.dao.CustomerDao;
import com.example.hxds.cst.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerDao customerDao;

    @Resource
    private MicroAppUtil microAppUtil;

    @Override
    @Transactional
    @GlobalTransactional
    public String registerNewCustomer(Map param) {
        String code = MapUtil.getStr(param, "code");
        String openId = microAppUtil.getOpenId(code);
        HashMap<String, Object> tempParam = new HashMap<>();
        tempParam.put("openId", openId);
        if (customerDao.hasCustomer(tempParam) != 0) {
            throw new HxdsException("该微信无法重复注册");
        }

        String tel = StrUtil.trim(MapUtil.getStr(param, "tel"));
        if (StrUtil.isNotBlank(tel) && StrUtil.isNotBlank(customerDao.searchCustomerIdByTel(tel))) {
            throw new HxdsException("该手机号已绑定其他账号");
        }

        param.put("openId", openId);
        param.put("photo", normalizePhoto(MapUtil.getStr(param, "photo")));
        customerDao.registerNewCustomer(param);
        return customerDao.searchCustomerId(openId);
    }

    @Override
    @Transactional
    @GlobalTransactional
    public String login(Map param) {
        String code = MapUtil.getStr(param, "code");
        String tel = StrUtil.trim(MapUtil.getStr(param, "tel"));
        String openId = microAppUtil.getOpenId(code);
        String customerId = customerDao.login(openId);

        if (StrUtil.isBlank(tel)) {
            return customerId != null ? customerId : "";
        }

        String telCustomerId = customerDao.searchCustomerIdByTel(tel);
        if (StrUtil.isNotBlank(customerId)) {
            if (StrUtil.isNotBlank(telCustomerId) && !telCustomerId.equals(customerId)) {
                throw new HxdsException("该手机号已绑定其他微信账号");
            }
            if (!customerId.equals(telCustomerId)) {
                HashMap<String, Object> updateParam = new HashMap<>();
                updateParam.put("customerId", ConvertCustomerId(customerId));
                updateParam.put("tel", tel);
                customerDao.updateCustomerTel(updateParam);
            }
            return customerId;
        }

        if (StrUtil.isNotBlank(telCustomerId)) {
            throw new HxdsException("该手机号已绑定其他微信账号，请使用原微信登录");
        }

        HashMap<String, Object> registerParam = new HashMap<>();
        registerParam.put("openId", openId);
        registerParam.put("nickname", buildDefaultNickname(tel));
        registerParam.put("sex", "未知");
        registerParam.put("photo", "");
        registerParam.put("tel", tel);
        customerDao.registerNewCustomer(registerParam);
        return customerDao.searchCustomerId(openId);
    }

    @Override
    public HashMap searchCustomerInfoInOrder(long customerId) {
        return customerDao.searchCustomerInfoInOrder(customerId);
    }

    @Override
    public HashMap searchCustomerBriefInfo(long customerId) {
        return customerDao.searchCustomerBriefInfo(customerId);
    }

    @Override
    public String searchCustomerOpenId(long customerId) {
        return customerDao.searchCustomerOpenId(customerId);
    }

    @Override
    public HashMap searchCustomerProfile(long customerId) {
        return customerDao.searchCustomerProfile(customerId);
    }

    @Override
    @Transactional
    public int updateCustomerProfile(Map param) {
        return customerDao.updateCustomerProfile(param);
    }

    @Override
    @Transactional
    public int updateCustomerPhoto(Map param) {
        return customerDao.updateCustomerPhoto(param);
    }

    @Override
    @Transactional
    public int updateCustomerTel(Map param) {
        String tel = StrUtil.trim(MapUtil.getStr(param, "tel"));
        String currentOwner = customerDao.searchCustomerIdByTel(tel);
        String customerId = String.valueOf(MapUtil.getLong(param, "customerId"));
        if (StrUtil.isNotBlank(currentOwner) && !currentOwner.equals(customerId)) {
            throw new HxdsException("该手机号已绑定其他账号");
        }
        return customerDao.updateCustomerTel(param);
    }

    private String normalizePhoto(String photo) {
        if (StrUtil.isBlank(photo) || photo.startsWith("http://") || photo.startsWith("https://")) {
            return StrUtil.nullToDefault(photo, "");
        }
        return "";
    }

    private String buildDefaultNickname(String tel) {
        String suffix = tel.length() >= 4 ? tel.substring(tel.length() - 4) : tel;
        return "微信用户" + suffix;
    }

    private Long ConvertCustomerId(String customerId) {
        return Long.parseLong(customerId);
    }
}
