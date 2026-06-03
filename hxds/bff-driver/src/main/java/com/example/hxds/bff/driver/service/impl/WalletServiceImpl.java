package com.example.hxds.bff.driver.service.impl;

import com.example.hxds.bff.driver.controller.form.SearchDriverWalletForm;
import com.example.hxds.bff.driver.feign.DrServiceApi;
import com.example.hxds.bff.driver.service.WalletService;
import com.example.hxds.common.util.R;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.HashMap;

@Service
public class WalletServiceImpl implements WalletService {

    @Resource
    private DrServiceApi drServiceApi;

    @Override
    public HashMap searchDriverWallet(SearchDriverWalletForm form) {
        R r = drServiceApi.searchDriverWallet(form);
        return (HashMap) r.get("result");
    }
}
