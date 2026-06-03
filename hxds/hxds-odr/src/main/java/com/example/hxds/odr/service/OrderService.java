package com.example.hxds.odr.service;

import com.example.hxds.common.util.PageUtils;
import com.example.hxds.odr.db.pojo.OrderBillEntity;
import com.example.hxds.odr.db.pojo.OrderEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface OrderService {

    //查询首页信息 查询当天代驾总时长、总收入和订单数。
    public HashMap searchDriverTodayBusinessData(long driverId);

    //创建代驾订单
    public String insertOrder(OrderEntity  orderEntity, OrderBillEntity orderBillEntity);

    //司机抢单 更新订单记录
    public String acceptNewOrder(long driverId, long orderId);

    //加载执行的订单详情
    public HashMap searchDriverExecuteOrder(Map param);

    //乘客端查询订单状态
    public Integer searchOrderStatus(Map param);

    //乘客端删除订单、抢单缓存、
    public String deleteUnAcceptOrder(Map param);

    //查询司机正在执行的订单
    public HashMap searchDriverCurrentOrder(long driverId);

    //查询没有司机接单的订单和没有完成的订单
    public HashMap hasCustomerCurrentOrder(long customerId);

    //根据orderId查询属于某个司机或者乘客的与司乘同显有关的订单信息
    public HashMap searchOrderForMoveById(Map param);

    //司机到达起始点，更新订单状态
    public int arriveStartPlace(Map param);

    //乘客端手动确认司机到达
    public boolean confirmArriveStartPlace(long orderId);

    //开始代驾
    public int startDriving(Map param);

    //结束代驾  更新订单状态
    public int updateOrderStatus(Map param);

    //mis系统查询订单分页记录
    public PageUtils searchOrderByPage(Map param);

    //mis查询订单详情信息  折叠面板
    public HashMap searchOrderContent(long orderId);

    //最近30天的代驾上车点坐标
    public ArrayList<HashMap> searchOrderStartLocationIn30Days();

    //查询司机与订单是否有关联
    public boolean validDriverOwnOrder(Map param);

    //查询订单的接单时间、开始代驾时间、代驾等时分钟数、好处费
    public HashMap searchSettlementNeedData(long driverId);

    //mq接收到新订单信息跳转到订单预览页面
    public HashMap searchOrderById(Map param);

    //查询订单是否可以支付
    public HashMap validCanPayOrder(Map param);

    //更新订单表的预支付ID
    public int updateOrderPrepayId(Map param);

    //小程序司机查询订单评价详情分页  查询记录总数和分页记录
    public PageUtils searchDriverOrderByPage(Map param);

    //小程序乘客查询订单评价详情分页  查询记录总数和分页记录
    public PageUtils searchCustomerOrderByPage(Map param);

    // Fix-2: 微信支付回调 - 根据uuid更新订单支付状态为已付款
    // 返回: 1=成功, 0=幂等(已支付), -1=事故(已关单但款已扣,需人工退款)
    public int updateOrderPayStatus(Map<String, Object> param);

}
