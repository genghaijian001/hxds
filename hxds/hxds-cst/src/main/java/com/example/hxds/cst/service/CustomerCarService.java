package com.example.hxds.cst.service;

import com.example.hxds.cst.db.pojo.CustomerCarEntity;

import java.util.ArrayList;
import java.util.HashMap;

public interface CustomerCarService {
    //添加车辆
    public void insertCustomerCar(CustomerCarEntity entity);
    //查询车辆
    public ArrayList<HashMap> searchCustomerCarList(long customerId);
    //删除车辆
    public int deleteCustomerCarById(long id);
}
