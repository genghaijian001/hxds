package com.example.hxds.bff.customer.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import com.example.hxds.bff.customer.controller.form.LoginForm;
import com.example.hxds.bff.customer.controller.form.RegisterNewCustomerForm;
import com.example.hxds.bff.customer.controller.form.SearchCustomerOpenIdForm;
import com.example.hxds.bff.customer.controller.form.UpdateCustomerPhotoForm;
import com.example.hxds.bff.customer.controller.form.UpdateCustomerProfileForm;
import com.example.hxds.bff.customer.controller.form.UpdateCustomerTelForm;
import com.example.hxds.bff.customer.feign.CstServiceApi;
import com.example.hxds.bff.customer.service.CustomerService;
import com.example.hxds.bff.customer.util.SmsUtil;
import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.common.util.CosUtil;
import com.example.hxds.common.util.R;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CstServiceApi cstServiceApi;

    @Resource
    private CosUtil cosUtil;

    @Resource
    private SmsUtil smsUtil;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @GlobalTransactional
    public long registerNewCustomer(RegisterNewCustomerForm form) {
        R r = cstServiceApi.registerNewCustomer(form);
        return Convert.toLong(r.get("userId"));
    }

    @Override
    public Long login(LoginForm form) {
        boolean useSmsLogin = StrUtil.isNotBlank(form.getTel()) || StrUtil.isNotBlank(form.getSmsCode());
        if (useSmsLogin) {
            if (StrUtil.isBlank(form.getTel()) || StrUtil.isBlank(form.getSmsCode())) {
                throw new HxdsException("请输入手机号和验证码");
            }
            verifySmsCode(form.getTel(), form.getSmsCode());
        }

        R r = cstServiceApi.login(form);
        String userId = MapUtil.getStr(r, "userId");
        if (StrUtil.isBlank(userId)) {
            return null;
        }

        if (useSmsLogin) {
            clearSmsCode(form.getTel());
        }
        return Convert.toLong(userId);
    }

    @Override
    public HashMap searchCustomerProfile() {
        long customerId = StpUtil.getLoginIdAsLong();
        SearchCustomerOpenIdForm form = new SearchCustomerOpenIdForm();
        form.setCustomerId(customerId);
        R r = cstServiceApi.searchCustomerProfile(form);
        return (HashMap) r.get("result");
    }

    @Override
    public int updateCustomerProfile(UpdateCustomerProfileForm form) {
        form.setCustomerId(StpUtil.getLoginIdAsLong());
        R r = cstServiceApi.updateCustomerProfile(form);
        return MapUtil.getInt(r, "rows");
    }

    @Override
    public String updateCustomerPhoto(MultipartFile file) throws IOException {
        HashMap map = cosUtil.uploadPublicFile(file, "/customer/photo/");
        String url = MapUtil.getStr(map, "url");
        UpdateCustomerPhotoForm form = new UpdateCustomerPhotoForm();
        form.setCustomerId(StpUtil.getLoginIdAsLong());
        form.setPhoto(url);
        cstServiceApi.updateCustomerPhoto(form);
        return url;
    }

    @Override
    public void sendSmsCode(String tel) {
        String cooldownKey = getCooldownKey(tel);
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(cooldownKey))) {
            throw new HxdsException("操作频繁，请60秒后再试");
        }
        String code = RandomUtil.randomNumbers(6);
        String codeKey = getCodeKey(tel);
        stringRedisTemplate.opsForValue().set(codeKey, code, 5, TimeUnit.MINUTES);
        stringRedisTemplate.opsForValue().set(cooldownKey, "1", 60, TimeUnit.SECONDS);
        smsUtil.sendSmsCode(tel, code);
    }

    @Override
    public int updateCustomerTel(UpdateCustomerTelForm form) {
        verifySmsCode(form.getTel(), form.getSmsCode());
        form.setCustomerId(StpUtil.getLoginIdAsLong());
        UpdateCustomerTelForm telForm = new UpdateCustomerTelForm();
        telForm.setCustomerId(form.getCustomerId());
        telForm.setTel(form.getTel());
        R r = cstServiceApi.updateCustomerTel(telForm);
        clearSmsCode(form.getTel());
        return MapUtil.getInt(r, "rows");
    }

    private void verifySmsCode(String tel, String smsCode) {
        String storedCode = stringRedisTemplate.opsForValue().get(getCodeKey(tel));
        if (storedCode == null) {
            throw new HxdsException("验证码已过期，请重新获取");
        }
        if (!storedCode.equals(smsCode)) {
            throw new HxdsException("验证码错误");
        }
    }

    private void clearSmsCode(String tel) {
        stringRedisTemplate.delete(getCodeKey(tel));
        stringRedisTemplate.delete(getCooldownKey(tel));
    }

    private String getCodeKey(String tel) {
        return "sms:code:" + tel;
    }

    private String getCooldownKey(String tel) {
        return "sms:cooldown:" + tel;
    }
}
