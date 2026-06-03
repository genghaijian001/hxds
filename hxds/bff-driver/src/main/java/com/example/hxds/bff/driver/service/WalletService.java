package com.example.hxds.bff.driver.service;

import com.example.hxds.bff.driver.controller.form.SearchDriverWalletForm;

import java.util.HashMap;

public interface WalletService {
    HashMap searchDriverWallet(SearchDriverWalletForm form);
}
