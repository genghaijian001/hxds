package com.example.hxds.dr.service.impl;

import cn.hutool.core.map.MapUtil;
import com.example.hxds.dr.db.dao.WalletDao;
import com.example.hxds.dr.db.dao.WalletIncomeDao;
import com.example.hxds.dr.db.dao.WalletPaymentDao;
import com.example.hxds.dr.service.WalletService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class WalletServiceImpl implements WalletService {

    @Resource
    private WalletDao walletDao;

    @Resource
    private WalletIncomeDao walletIncomeDao;

    @Resource
    private WalletPaymentDao walletPaymentDao;

    @Override
    public HashMap searchDriverWallet(long driverId, String month) {
        // Get balance
        HashMap wallet = walletDao.searchWalletByDriverId(driverId);
        String balance = MapUtil.getStr(wallet, "balance");

        // Income total in month
        Map<String, Object> param = new HashMap<>();
        param.put("driverId", driverId);
        param.put("month", month);
        BigDecimal incomeTotalInMonth = walletIncomeDao.sumIncomeInMonth(param);

        // Today's income
        param.put("day", LocalDate.now().toString());
        BigDecimal incomeTotalInDay = walletIncomeDao.sumIncomeInDay(param);

        // Income records in month
        ArrayList<HashMap> incomeList = walletIncomeDao.searchIncomeByMonth(param);
        // Payment records in month
        ArrayList<HashMap> paymentList = walletPaymentDao.searchPaymentByMonth(param);

        // Merge and label
        ArrayList<HashMap> recordInMonth = new ArrayList<>();
        for (HashMap one : incomeList) {
            one.put("name", "入账");
            recordInMonth.add(one);
        }

        // Calculate payment total
        BigDecimal paymentTotalInMonth = BigDecimal.ZERO;
        for (HashMap one : paymentList) {
            String amt = MapUtil.getStr(one, "amount");
            if (amt != null) {
                paymentTotalInMonth = paymentTotalInMonth.add(new BigDecimal(amt));
            }
            one.put("name", "支出");
            recordInMonth.add(one);
        }

        HashMap result = new HashMap();
        result.put("balance", balance);
        result.put("incomeTotalInMonth", incomeTotalInMonth.toPlainString());
        result.put("paymentTotalInMonth", paymentTotalInMonth.toPlainString());
        result.put("incomeTotalInDay", incomeTotalInDay.toPlainString());
        result.put("recordInMonth", recordInMonth);
        return result;
    }
}
