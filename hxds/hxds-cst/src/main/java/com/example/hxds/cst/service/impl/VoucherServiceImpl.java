package com.example.hxds.cst.service.impl;

import com.example.hxds.cst.db.dao.VoucherDao;
import com.example.hxds.cst.service.VoucherService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class VoucherServiceImpl implements VoucherService {

    @Resource
    private VoucherDao voucherDao;

    @Override
    public long searchUnUseVoucherCount(long customerId) {
        return voucherDao.searchUnUseVoucherCount(customerId);
    }

    @Override
    public HashMap searchUnUseVoucherByPage(Map param) {
        int page = (int) param.get("page");
        int length = (int) param.get("length");
        long start = (long) (page - 1) * length;
        param.put("start", start);
        ArrayList list = voucherDao.searchUnUseVoucherByPage(param);
        long totalCount = voucherDao.searchUnUseVoucherByPageCount(param);
        HashMap result = new HashMap();
        result.put("list", list);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    public HashMap searchUsedVoucherByPage(Map param) {
        int page = (int) param.get("page");
        int length = (int) param.get("length");
        long start = (long) (page - 1) * length;
        param.put("start", start);
        ArrayList list = voucherDao.searchUsedVoucherByPage(param);
        long totalCount = voucherDao.searchUsedVoucherByPageCount(param);
        HashMap result = new HashMap();
        result.put("list", list);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    public HashMap searchUnTakeVoucherByPage(Map param) {
        int page = (int) param.get("page");
        int length = (int) param.get("length");
        long start = (long) (page - 1) * length;
        param.put("start", start);
        ArrayList list = voucherDao.searchUnTakeVoucherByPage(param);
        long totalCount = voucherDao.searchUnTakeVoucherByPageCount(param);
        HashMap result = new HashMap();
        result.put("list", list);
        result.put("totalCount", totalCount);
        return result;
    }

    @Override
    public int takeVoucher(Map param) {
        return voucherDao.takeVoucher(param);
    }
}
