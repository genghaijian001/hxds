package com.example.hxds.bff.driver.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.example.hxds.bff.driver.controller.form.SearchDriverWalletForm;
import com.example.hxds.bff.driver.service.WalletService;
import com.example.hxds.common.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/wallet")
@Tag(name = "WalletController", description = "司机钱包Web接口")
public class WalletController {

    @Resource
    private WalletService walletService;

    @PostMapping("/searchDriverWallet")
    @Operation(summary = "查询司机钱包")
    @SaCheckLogin
    public R searchDriverWallet(@RequestBody @Valid SearchDriverWalletForm form) {
        long driverId = StpUtil.getLoginIdAsLong();
        form.setDriverId(driverId);
        HashMap result = walletService.searchDriverWallet(form);
        return R.ok().put("result", result);
    }
}
