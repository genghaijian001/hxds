package com.example.hxds.nebula.db.dao;

import com.example.hxds.nebula.db.pojo.OrderMonitoringEntity;

import java.util.HashMap;
import java.util.Map;

public interface OrderMonitoringDao {

    //利用AI对司乘对话内容安全评级 添加订单监控摘要记录
    public int insert(long orderId);
    //查询订单录音数量、需要人工审核的数量
    public HashMap searchOrderRecordsAndReviews(long orderId);
    //更新订单监测
    public int updateOrderMonitoring(OrderMonitoringEntity entity);
}
