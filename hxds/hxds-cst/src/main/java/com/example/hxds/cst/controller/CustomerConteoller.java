package com.example.hxds.cst.controller;

import cn.hutool.core.bean.BeanUtil;
import com.example.hxds.common.util.R;
import com.example.hxds.cst.controller.form.LoginForm;
import com.example.hxds.cst.controller.form.RegisterNewCustomerForm;
import com.example.hxds.cst.controller.form.SearchCustomerBriefInfoForm;
import com.example.hxds.cst.controller.form.SearchCustomerInfoInOrderForm;
import com.example.hxds.cst.controller.form.SearchCustomerOpenIdForm;
import com.example.hxds.cst.controller.form.UpdateCustomerPhotoForm;
import com.example.hxds.cst.controller.form.UpdateCustomerProfileForm;
import com.example.hxds.cst.controller.form.UpdateCustomerTelForm;
import com.example.hxds.cst.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/customer")
@Tag(name = "CustomerController", description = "Customer web API")
public class CustomerConteoller {

    @Resource
    private CustomerService customerService;

    @PostMapping("/registerNewCustomer")
    @Operation(summary = "Register customer")
    public R registerNewCustomer(@RequestBody @Valid RegisterNewCustomerForm form) {
        Map param = BeanUtil.beanToMap(form);
        String userId = customerService.registerNewCustomer(param);
        return R.ok().put("userId", userId);
    }

    @PostMapping("/login")
    @Operation(summary = "Login customer")
    public R login(@RequestBody @Valid LoginForm form) {
        Map param = BeanUtil.beanToMap(form);
        String userId = customerService.login(param);
        return R.ok().put("userId", userId);
    }

    @PostMapping("/searchCustomerInfoInOrder")
    @Operation(summary = "Search customer info in order")
    public R searchCustomerInfoInOrder(@RequestBody @Valid SearchCustomerInfoInOrderForm form) {
        HashMap map = customerService.searchCustomerInfoInOrder(form.getCustomerId());
        return R.ok().put("result", map);
    }

    @PostMapping("/searchCustomerBriefInfo")
    @Operation(summary = "Search customer brief info")
    public R searchCustomerBriefInfo(@RequestBody @Valid SearchCustomerBriefInfoForm form) {
        HashMap map = customerService.searchCustomerBriefInfo(form.getCustomerId());
        return R.ok().put("result", map);
    }

    @PostMapping("/searchCustomerOpenId")
    @Operation(summary = "Search customer openId")
    public R searchCustomerOpenId(@RequestBody @Valid SearchCustomerOpenIdForm form) {
        String openId = customerService.searchCustomerOpenId(form.getCustomerId());
        return R.ok().put("result", openId);
    }

    @PostMapping("/searchCustomerProfile")
    @Operation(summary = "Search customer profile")
    public R searchCustomerProfile(@RequestBody @Valid SearchCustomerBriefInfoForm form) {
        HashMap map = customerService.searchCustomerProfile(form.getCustomerId());
        return R.ok().put("result", map);
    }

    @PostMapping("/updateCustomerProfile")
    @Operation(summary = "Update customer profile")
    public R updateCustomerProfile(@RequestBody @Valid UpdateCustomerProfileForm form) {
        Map param = BeanUtil.beanToMap(form);
        int rows = customerService.updateCustomerProfile(param);
        return R.ok().put("rows", rows);
    }

    @PostMapping("/updateCustomerPhoto")
    @Operation(summary = "Update customer photo")
    public R updateCustomerPhoto(@RequestBody @Valid UpdateCustomerPhotoForm form) {
        Map param = BeanUtil.beanToMap(form);
        int rows = customerService.updateCustomerPhoto(param);
        return R.ok().put("rows", rows);
    }

    @PostMapping("/updateCustomerTel")
    @Operation(summary = "Update customer tel")
    public R updateCustomerTel(@RequestBody @Valid UpdateCustomerTelForm form) {
        Map param = BeanUtil.beanToMap(form);
        int rows = customerService.updateCustomerTel(param);
        return R.ok().put("rows", rows);
    }
}
