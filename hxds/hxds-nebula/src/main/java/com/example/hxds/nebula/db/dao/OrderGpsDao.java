package com.example.hxds.nebula.db.dao;

import com.example.hxds.nebula.db.pojo.OrderGpsEntity;
import org.apache.hadoop.util.hash.Hash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface OrderGpsDao {

    //添加订单GPS记录
    public int insert(OrderGpsEntity entity);

    //mis查询订单详情地图坐标
    public ArrayList<HashMap> searchOrderGps(long orderId);

    //mis查询订单详情地图坐标  结束后的坐标
    public HashMap searchOrderLastGps(long orderId);

    //查询订单中的所有经纬度
    public ArrayList<HashMap> searchOrderAllGps(long orderId);

}
