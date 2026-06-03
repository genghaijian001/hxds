package com.example.hxds.cst.service.impl;

import io.seata.spring.annotation.GlobalTransactional;
import com.example.hxds.cst.db.dao.CustomerCarDao;
import com.example.hxds.cst.db.pojo.CustomerCarEntity;
import com.example.hxds.cst.service.CustomerCarService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class CustomerCarServiceImpl implements CustomerCarService {

    @Resource
    private CustomerCarDao customerCarDao;

    @Override
    @Transactional
    @GlobalTransactional
    public void insertCustomerCar(CustomerCarEntity entity) {
        customerCarDao.insert(entity); //添加车辆
    }

    @Override
    public ArrayList<HashMap> searchCustomerCarList(long customerId) {
        ArrayList list = customerCarDao.searchCustomerCarList(customerId);  //查询车辆
        return list;
    }

    @Override
    @Transactional
    @GlobalTransactional
    public int deleteCustomerCarById(long id) {
        int rows = customerCarDao.deleteCustomerCarById(id);  //删除车辆
        return rows;
    }
}
