package com.example.hxds.bff.customer.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.hxds.bff.customer.controller.form.LoginForm;
import com.example.hxds.bff.customer.controller.form.RegisterNewCustomerForm;
import com.example.hxds.bff.customer.controller.form.SendSmsCodeForm;
import com.example.hxds.bff.customer.controller.form.UpdateCustomerProfileForm;
import com.example.hxds.bff.customer.controller.form.UpdateCustomerTelForm;
import com.example.hxds.bff.customer.service.CustomerService;
import com.example.hxds.common.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/customer")
@Tag(name = "CustomerController", description = "客户Web接口")
public class CustomerConteoller {

    @Resource
    private CustomerService customerService;

    @PostMapping("/registerNewCustomer")
    @Operation(summary = "注册新司机")
    public R registerNewCustomer(@RequestBody @Valid RegisterNewCustomerForm form) {
        long customerId = customerService.registerNewCustomer(form);
        StpUtil.login(customerId);
        String token = StpUtil.getTokenInfo().getTokenValue();
        return R.ok().put("token", token);
    }

    @PostMapping("/searchCustomerProfile")
    @Operation(summary = "查询当前登录客户的个人资料")
    public R searchCustomerProfile() {
        HashMap map = customerService.searchCustomerProfile();
        return R.ok().put("result", map);
    }

    @PostMapping("/login")
    @Operation(summary = "登陆系统")
    public R login(@RequestBody @Valid LoginForm form) {
        Long customerId = customerService.login(form);
        if (customerId == null) {
            return R.ok();
        }
        StpUtil.login(customerId);
        String token = StpUtil.getTokenInfo().getTokenValue();
        return R.ok().put("token", token);
    }

    @PostMapping("/updateCustomerProfile")
    @Operation(summary = "更新客户昵称和性别")
    public R updateCustomerProfile(@RequestBody @Valid UpdateCustomerProfileForm form) {
        int rows = customerService.updateCustomerProfile(form);
        return R.ok().put("rows", rows);
    }

    @PostMapping("/updateCustomerPhoto")
    @Operation(summary = "更新客户头像")
    public R updateCustomerPhoto(@RequestParam("file") MultipartFile file) throws IOException {
        String url = customerService.updateCustomerPhoto(file);
        return R.ok().put("result", url);
    }

    @PostMapping("/sendSmsCode")
    @Operation(summary = "发送短信验证码")
    public R sendSmsCode(@RequestBody @Valid SendSmsCodeForm form) {
        customerService.sendSmsCode(form.getTel());
        return R.ok();
    }

    @PostMapping("/updateCustomerTel")
    @Operation(summary = "更新客户手机号")
    public R updateCustomerTel(@RequestBody @Valid UpdateCustomerTelForm form) {
        int rows = customerService.updateCustomerTel(form);
        return R.ok().put("rows", rows);
    }
}
