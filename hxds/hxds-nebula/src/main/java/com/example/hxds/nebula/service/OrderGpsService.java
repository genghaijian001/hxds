package com.example.hxds.nebula.service;

import com.example.hxds.nebula.controller.vo.InsertOrderGpsVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface OrderGpsService {

    //添加订单GPS记录
    public int insertOrderGps(ArrayList<InsertOrderGpsVo> list);

    //mis查询订单详情地图坐标
    public ArrayList<HashMap> searchOrderGps(long orderId);

    //mis查询订单详情地图坐标  结束后的坐标
    public HashMap searchOrderLastGps(long orderId);

    //查询订单中的所有经纬度
    public String calculateOrderMileage(long orderId);
}
