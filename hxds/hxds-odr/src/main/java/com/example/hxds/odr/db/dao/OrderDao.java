package com.example.hxds.odr.db.dao;

import com.example.hxds.odr.db.pojo.OrderEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface OrderDao {

    //司机微服务中查询首页信息 查询当天代驾总时长、总收入和订单数。
    public HashMap searchDriverTodayBusinessData(long driverId);
    //创建代驾订单
    public int insert(OrderEntity entity);
    //订单号uuid
    public String searchOrderIdByUUID(String uuid);
    //司机抢单 更新订单记录
    public int acceptNewOrder(Map param);
    //加载执行的订单详情
    public HashMap searchDriverExecuteOrder(Map param);
    //乘客端查询订单状态
    public Integer searchOrderStatus(Map param);
    //乘客端删除订单、抢单缓存、
    public int deleteUnAcceptOrder(Map param);
    //查询司机正在执行的订单
    public HashMap searchDriverCurrentOrder(long driverId);
    //查询没有司机接单的订单
    public Long hasCustomerUnFinishedOrder(long customerId);
    //和没有完成的订单
    public HashMap hasCustomerUnAcceptOrder(long customerId);
    //根据orderId查询属于某个司机或者乘客的与司乘同显有关的订单信息
    public HashMap searchOrderForMoveById(Map param);
    //司机到达起始点，更新订单状态
    public int updateOrderStatus(Map param);

    //mis系统查询订单分页记录
    public ArrayList<HashMap> searchOrderByPage(Map param);

    //mis系统查询订单分页记录  总数
    public long searchOrderCount(Map param);

    //mis查询订单详情信息  折叠面板
    public HashMap searchOrderContent(long orderId);

    //最近30天的代驾上车点坐标
    public ArrayList<String> searchOrderStartLocationIn30Days();

    //更新订单、账单和分账记录
    public int updateOrderMileageAndFee(Map param);

    //查询司机与订单是否有关联
    public long validDriverOwnOrder(Map param);

    //查询订单的接单时间、开始代驾时间、代驾等时分钟数、好处费
    public HashMap searchSettlementNeedData(long driverId);

    //mq接收到新订单信息跳转到订单预览页面
    public HashMap searchOrderById(Map param);

    //查询订单是否可以支付
    public HashMap validCanPayOrder(Map param);

    //更新订单表的预支付ID
    public int updateOrderPrepayId(Map param);

    //查询司机与乘客跟订单是否有关联关系
    public long validDriverAndCustomerOwnOrder(Map param);

    //小程序司机查询订单评价详情分页  查询记录总数和分页记录
    public ArrayList<HashMap> searchDriverOrderByPage(Map param);
    public long searchDriverOrderCount(Map param);

    //小程序乘客查询订单评价详情分页  查询记录总数和分页记录
    public ArrayList<HashMap> searchCustomerOrderByPage(Map param);
    public long searchCustomerOrderCount(Map param);

    // Fix-2: 微信支付回调 - 根据uuid更新订单支付状态
    public int updateOrderPayStatus(Map<String, Object> param);

    // INC-3: 关闭超过30分钟未支付的订单(status=6 → status=9)
    public int closeUnpaidTimeoutOrders();

}




