package com.example.hxds.dr.controller;

import com.example.hxds.common.util.R;
import com.example.hxds.dr.controller.form.SearchDriverWalletForm;
import com.example.hxds.dr.service.WalletService;
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
    public R searchDriverWallet(@RequestBody @Valid SearchDriverWalletForm form) {
        HashMap result = walletService.searchDriverWallet(form.getDriverId(), form.getMonth());
        return R.ok().put("result", result);
    }
}
