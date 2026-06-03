package com.example.hxds.odr.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.seata.spring.annotation.GlobalTransactional;
import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.common.util.PageUtils;
import com.example.hxds.common.wxpay.MyWXPayConfig;
import com.example.hxds.odr.controller.form.TransferForm;
import com.example.hxds.odr.db.dao.OrderBillDao;
import com.example.hxds.odr.db.dao.OrderDao;
import com.example.hxds.odr.db.dao.OrderProfitsharingDao;
import com.example.hxds.odr.db.pojo.OrderBillEntity;
import com.example.hxds.odr.db.pojo.OrderEntity;
import com.example.hxds.odr.db.pojo.OrderProfitsharingEntity;
import com.example.hxds.odr.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    /**
     * Fix-3: 订单状态合法转移表
     * 1等待接单 2已接单 3司机已到达 4开始代驾 5结束代驾
     * 6未付款 7已付款 8订单已结束 9顾客撤单 10司机撤单 11事故关闭
     */
    private static final Map<Byte, Set<Byte>> VALID_TRANSITIONS = new HashMap<>();
    static {
        VALID_TRANSITIONS.put((byte) 1, new HashSet<>(Arrays.asList((byte) 2, (byte) 9, (byte) 10)));
        VALID_TRANSITIONS.put((byte) 2, new HashSet<>(Arrays.asList((byte) 3, (byte) 9, (byte) 10)));
        VALID_TRANSITIONS.put((byte) 3, new HashSet<>(Arrays.asList((byte) 4, (byte) 9, (byte) 10, (byte) 11)));
        // DESIGN-2修复: 状态4(行驶中)可转为5(结束)或11(事故关闭)
        VALID_TRANSITIONS.put((byte) 4, new HashSet<>(Arrays.asList((byte) 5, (byte) 11)));
        VALID_TRANSITIONS.put((byte) 5, new HashSet<>(Collections.singletonList((byte) 6)));
        VALID_TRANSITIONS.put((byte) 6, new HashSet<>(Collections.singletonList((byte) 7)));
        VALID_TRANSITIONS.put((byte) 7, new HashSet<>(Collections.singletonList((byte) 8)));
    }

    /** Fix-3: 校验状态转移是否合法 */
    private void validateStatusTransition(byte currentStatus, byte targetStatus) {
        Set<Byte> allowedTargets = VALID_TRANSITIONS.get(currentStatus);
        if (allowedTargets == null || !allowedTargets.contains(targetStatus)) {
            throw new HxdsException(
                String.format("非法的订单状态转移：%d -> %d", currentStatus, targetStatus)
            );
        }
    }

    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderBillDao orderBillDao;

    @Resource
    private RedisTemplate redisTemplate;

//    @Resource
//    private DrServiceApi drServiceApi;
//
//    @Resource
//    private QuartzUtil quartzUtil;

    @Resource
    private MyWXPayConfig myWXPayConfig;

    /**
     * 司机微服务中查询首页信息 查询当天代驾总时长、总收入和订单数。
     * @param driverId
     * @return
     */
    @Override
    public HashMap searchDriverTodayBusinessData(long driverId) {
        HashMap result = orderDao.searchDriverTodayBusinessData(driverId);
        return result;
    }

    /**
     * 创建代驾订单
     * @param orderEntity
     * @param orderBillEntity
     * @return
     */
    @Override
    @Transactional
    @GlobalTransactional
    public String insertOrder(OrderEntity orderEntity, OrderBillEntity orderBillEntity) {
        //插入订单记录
        int rows = orderDao.insert(orderEntity);
        if (rows == 1){
            String id = orderDao.searchOrderIdByUUID(orderEntity.getUuid());
            //插入订单费用记录
            orderBillEntity.setOrderId(Long.parseLong(id));
            rows = orderBillDao.insert(orderBillEntity);
            if (rows == 1){
                //往Redis里面插入缓存，配合Redis事务用于司机抢单，避免多个司机同时抢单成功「
                redisTemplate.opsForValue().set("order#" + id, "none");
                redisTemplate.expire("order#" + id, 16, TimeUnit.MINUTES); //缓存16分钟
                return id;
            }else {
                throw new HxdsException("保存新订单费用失败");
            }
        }else {
            throw new HxdsException("保存新订单失败");
        }
    }

    /**
     * 司机抢单 更新订单记录
     * @param driverId
     * @param orderId
     * @return
     */
    @Override
    @Transactional
    @GlobalTransactional
    public String acceptNewOrder(long driverId, long orderId) {
        if (!redisTemplate.hasKey("order#" + orderId)){
            return "抢单失败";
        }
        //执行redis事务（WATCH/MULTI/EXEC 乐观锁），防止多司机同时抢同一订单
        java.util.List<?> txResult = (java.util.List<?>) redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //获取新订单记录的Version
                operations.watch("order#" + orderId);
                //本地缓存Redis操作
                operations.multi();
                //把新订单缓存的Value设置成抢单司机的ID
                operations.opsForValue().set("order#" + orderId, driverId + "");
                //执行Redis事务，其他司机并发修改时会返回null，表示事务失败
                return operations.exec();
            }
        });
        // Fix-1: 检查事务结果，exec()返回null或空列表说明被其他司机抢先，本次抢单失败
        if (txResult == null || txResult.isEmpty()) {
            return "抢单失败";
        }
        // DESIGN-1修复: 先更新DB，成功后再删除Redis缓存
        // 原逻辑先删缓存再更新DB，DB失败时缓存已删导致订单无法被其他司机抢
        HashMap param=new HashMap(){{
            put("driverId" , driverId);
            put("orderId" , orderId);
        }};
        int rows = orderDao.acceptNewOrder(param);
        if (rows != 1){
            throw new HxdsException("接单失败，无法更新订单记录");
        }
        // DB更新成功后才删除Redis缓存，防止竞态条件下数据不一致
        redisTemplate.delete("order#" + orderId);
        return "接单成功";
    }

    /**
     * 加载执行的订单详情
     * @param param
     * @return
     */
    @Override
    public HashMap searchDriverExecuteOrder(Map param) {
        HashMap map = orderDao.searchDriverExecuteOrder(param);
        return map;
    }

    /**
     * 乘客端查询订单状态
     * @param param
     * @return
     */
    @Override
    public Integer searchOrderStatus(Map param) {
        Integer status = orderDao.searchOrderStatus(param);
        if (status == null) {
            status=0;
        }
        return status;
    }

    /**
     * 乘客端删除订单、抢单缓存、
     * @param param
     * @return
     */
    @Override
    @Transactional
    @GlobalTransactional
    public String deleteUnAcceptOrder(Map param) {
        long orderId = MapUtil.getLong(param, "orderId");
        if (!redisTemplate.hasKey("order#"+orderId)){
            return "订单取消失败";
        }
        //执行redis事务
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //获取新订单记录的Version
                operations.watch("order#"+orderId);
                //本地缓存Redis操作
                operations.multi();
                //把新订单缓存的Value设置成none
                operations.opsForValue().set("order#"+orderId,"none");
                //执行Redis事务，如果事务提交失败会自动抛出异常
                return operations.exec();
            }
        });
        redisTemplate.delete("order#"+orderId);
        int rows = orderDao.deleteUnAcceptOrder(param);
        if (rows != 1) {
            return "订单取消失败";
        }
        rows = orderBillDao.deleteUnAcceptOrderBill(orderId);
        if (rows != 1) {
            return "账单取消失败";
        }
        return "订单取消成功";
    }

    /**
     * 查询司机正在执行的订单
     * @param driverId
     * @return
     */
    @Override
    public HashMap searchDriverCurrentOrder(long driverId) {
        HashMap map = orderDao.searchDriverCurrentOrder(driverId);
        return map;
    }

    /**
     * 查询没有司机接单的订单和没有完成的订单
     * @param customerId
     * @return
     */
    @Override
    public HashMap hasCustomerCurrentOrder(long customerId) {
        HashMap result=new HashMap();
        //没有完成的订单
        HashMap map = orderDao.hasCustomerUnAcceptOrder(customerId);
        result.put("hasCustomerUnAcceptOrder", map != null);
        result.put("unAcceptOrder", map);
        //没有司机接单的订单
        Long id = orderDao.hasCustomerUnFinishedOrder(customerId);
        result.put("hasCustomerUnFinishedOrder", id != null);
        result.put("unFinishedOrder", id);

        return result;
    }

    /**
     * 根据orderId查询属于某个司机或者乘客的与司乘同显有关的订单信息
     * @param param
     * @return
     */
    @Override
    public HashMap searchOrderForMoveById(Map param) {
        HashMap map = orderDao.searchOrderForMoveById(param);
        return map;
    }

    /**
     * 司机到达起始点，更新订单状态
     * @param param
     * @return
     */
    @Override
    @Transactional
    @GlobalTransactional
    public int arriveStartPlace(Map param) {
        //添加到达上车点标志位，INC-5修复: 设置30分钟TTL防止乘客不确认导致订单永久卡在状态3
        long orderId = MapUtil.getLong(param, "orderId");
        redisTemplate.opsForValue().set("order_driver_arrivied#" + orderId, "1", 30, TimeUnit.MINUTES);
        int rows = orderDao.updateOrderStatus(param);
        if (rows != 1) {
            throw new HxdsException("更新订单状态失败");
        }
        return rows;
    }

    /**
     * 乘客端手动确认司机到达
     * @param orderId
     * @return
     */
    @Override
    public boolean confirmArriveStartPlace(long orderId) {
        String key="order_driver_arrivied#"+orderId;
        if (redisTemplate.hasKey(key)) {
            if (redisTemplate.opsForValue().get(key).toString().equals("1")) {
                redisTemplate.opsForValue().set(key, "2", 10, TimeUnit.MINUTES);
                return true;
            }
            return false;
        }
        // key已过期，查DB：status=3说明司机确实已到达，补设"2"允许继续流程
        HashMap<String, Object> statusParam = new HashMap<>();
        statusParam.put("orderId", orderId);
        Integer status = orderDao.searchOrderStatus(statusParam);
        if (status != null && status == 3) {
            redisTemplate.opsForValue().set(key, "2", 10, TimeUnit.MINUTES);
            return true;
        }
        return false;
    }

    /**
     * 开始代驾
     * @param param
     * @return
     */
    @Override
    @Transactional
    public int startDriving(Map param) {
        long orderId = MapUtil.getLong(param, "orderId");
        String key = "order_driver_arrivied#" + orderId;
        boolean canStart = false;
        if (redisTemplate.hasKey(key)) {
            Object val = redisTemplate.opsForValue().get(key);
            if (val != null && "2".equals(val.toString())) {
                canStart = true;
            }
        }
        // Redis key 不存在时（已过期或序列化不稳定），查 DB 做回退：status=3 说明乘客已确认，允许开始
        if (!canStart) {
            HashMap<String, Object> statusParam = new HashMap<>();
            statusParam.put("orderId", orderId);
            Integer dbStatus = orderDao.searchOrderStatus(statusParam);
            if (dbStatus != null && dbStatus == 3) {
                canStart = true;
            }
        }
        if (canStart) {
            redisTemplate.delete(key);
            int rows = orderDao.updateOrderStatus(param);
            if (rows != 1) {
                throw new HxdsException("更新订单状态失败");
            }
            return rows;
        }
        throw new HxdsException("乘客未确认司机到达，无法开始代驾");
    }

    /**
     * 结束代驾  更新订单状态
     * @param param
     * @return
     */
    @Override
    @Transactional
    @GlobalTransactional
    public int updateOrderStatus(Map param) {
        long orderId = MapUtil.getLong(param, "orderId");
        byte targetStatus = ((Number) param.get("status")).byteValue();
        HashMap<String, Object> statusParam = new HashMap<>();
        statusParam.put("orderId", orderId);
        Integer currentStatus = orderDao.searchOrderStatus(statusParam);
        if (currentStatus != null) {
            validateStatusTransition(currentStatus.byteValue(), targetStatus);
        }
        int rows = orderDao.updateOrderStatus(param);
        if (rows != 1){
            throw new HxdsException("更新订单状态失败");
        }
        return rows;
    }

    /**
     * mis系统查询订单分页记录
     * @param param
     * @return
     */
    @Override
    public PageUtils searchOrderByPage(Map param) {
        long count = orderDao.searchOrderCount(param);

        ArrayList<HashMap> list=null;
        if (count==0) {
            list=new ArrayList<>();
        }else {
            list=orderDao.searchOrderByPage(param);
        }
        int start = (Integer) param.get("start");
        int length = (Integer) param.get("length");
        PageUtils pageUtils=new PageUtils(list,count, start, length);
        return pageUtils;
    }

    /**
     * mis查询订单详情信息  折叠面板
     * @param orderId
     * @return
     */
    @Override
    public HashMap searchOrderContent(long orderId) {
        HashMap map = orderDao.searchOrderContent(orderId);
        //通过MapUtil工具类的getStr方法从名为"startPlaceLocation"的键对应的值
        //从map中获取的字符串转换为一个JSONObject对象
        JSONObject startPlaceLocation = JSONUtil.parseObj(MapUtil.getStr(map, "startPlaceLocation"));
        JSONObject endPlaceLocation = JSONUtil.parseObj(MapUtil.getStr(map, "endPlaceLocation"));

        map.replace("startPlaceLocation",startPlaceLocation);
        map.replace("endPlaceLocation",endPlaceLocation);
        return map;
    }

    /**
     * 最近30天的代驾上车点坐标  生成热点图
     * @return
     */
    @Override
    public ArrayList<HashMap> searchOrderStartLocationIn30Days() {
        ArrayList<String> list = orderDao.searchOrderStartLocationIn30Days();
        ArrayList<HashMap> result=new ArrayList();
        list.forEach(location -> {
            JSONObject json = JSONUtil.parseObj(location);
            String latitude = json.getStr("latitude");
            String longitude = json.getStr("longitude");
            //从字符串的开头开始，截取到倒数第四个字符（不包括倒数第四个字符本身），得到一个新的字符串
            latitude=latitude.substring(0,latitude.length()-4);
            latitude+="0001";
            longitude=longitude.substring(0,longitude.length()-4);
            longitude+="0001";
            HashMap map=new HashMap();
            map.put("latitude",latitude);
            map.put("longitude",longitude);
            result.add(map);
        });
        return result;
    }

    @Override
    public boolean validDriverOwnOrder(Map param) {
        long count = orderDao.validDriverOwnOrder(param);
        return count == 1 ? true : false;
    }

    @Override
    public HashMap searchSettlementNeedData(long driverId) {
        HashMap map = orderDao.searchSettlementNeedData(driverId);
        return map;
    }

    @Override
    public HashMap searchOrderById(Map param) {
        HashMap map = orderDao.searchOrderById(param);
        String startPlaceLocation = MapUtil.getStr(map, "startPlaceLocation");
        String endPlaceLocation = MapUtil.getStr(map, "endPlaceLocation");
        map.replace("startPlaceLocation",JSONUtil.parseObj(startPlaceLocation));
        map.replace("endPlaceLocation",JSONUtil.parseObj(endPlaceLocation));
        return map;
    }

    @Override
    public HashMap validCanPayOrder(Map param) {
        HashMap map = orderDao.validCanPayOrder(param);
        if (map == null || map.size() == 0) {
            throw new HxdsException("订单无法支付");
        }
        return map;
    }

    @Override
    @Transactional
    @GlobalTransactional
    public int updateOrderPrepayId(Map param) {
        int rows = orderDao.updateOrderPrepayId(param);
        if (rows != 1){
            throw new HxdsException("更新预支付ID失败");
        }
        return rows;
    }

    @Override
    public PageUtils searchDriverOrderByPage(Map param) {
        long count = orderDao.searchDriverOrderCount(param);
        ArrayList<HashMap> list = null;
        if (count > 0){
            list = orderDao.searchDriverOrderByPage(param);
        }else {
            list = new ArrayList();
        }
        int  start = (Integer) param.get("start");
        int  length = (Integer) param.get("length");
        PageUtils pageUtils = new PageUtils(list,count,start,length);
        return pageUtils;
    }

    @Override
    public PageUtils searchCustomerOrderByPage(Map param) {
        long count = orderDao.searchCustomerOrderCount(param);
        ArrayList<HashMap> list = null;
        if (count > 0){
            list = orderDao.searchCustomerOrderByPage(param);
        }else {
            list = new ArrayList();
        }
        int  start = (Integer) param.get("start");
        int  length = (Integer) param.get("length");
        PageUtils pageUtils = new PageUtils(list,count,start,length);
        return pageUtils;
    }

    /**
     * Fix-2 / INC-1修复: 微信支付回调处理
     * 1. 更新 tb_order 状态为7(已付款)、pay_id、pay_time
     * 2. 同步更新 tb_order_bill.real_pay，保持账单与订单一致
     */
    @Override
    @Transactional
    public int updateOrderPayStatus(Map<String, Object> param) {
        int rows = orderDao.updateOrderPayStatus(param);
        if (rows == 1) {
            log.info("微信支付回调：订单支付成功，uuid={}, payId={}", param.get("uuid"), param.get("transactionId"));
            String realPay = param.get("realPay") != null ? param.get("realPay").toString() : null;
            if (realPay != null) {
                String uuid = param.get("uuid").toString();
                String orderIdStr = orderDao.searchOrderIdByUUID(uuid);
                if (orderIdStr != null) {
                    HashMap billParam = new HashMap();
                    billParam.put("orderId", Long.parseLong(orderIdStr));
                    billParam.put("realPay", realPay);
                    billParam.put("voucherFee", "0.00");
                    orderBillDao.updateBillPayment(billParam);
                }
            }
            return 1;
        }
        // rows=0：查询订单实际状态，区分幂等与业务事故
        String uuid = param.get("uuid") != null ? param.get("uuid").toString() : "";
        String orderIdStr = orderDao.searchOrderIdByUUID(uuid);
        if (orderIdStr == null) {
            log.warn("微信支付回调：找不到订单记录，uuid={}", uuid);
            return 0;
        }
        HashMap<String, Object> statusParam = new HashMap<>();
        statusParam.put("orderId", Long.parseLong(orderIdStr));
        Integer currentStatus = orderDao.searchOrderStatus(statusParam);
        if (currentStatus == null) {
            log.warn("微信支付回调：查询订单状态为空，uuid={}", uuid);
            return 0;
        }
        if (currentStatus == 7) {
            log.info("微信支付回调：幂等处理，订单已支付，uuid={}", uuid);
            return 0;
        }
        if (currentStatus == 9) {
            // 超时关单后微信仍扣款 — 业务事故，需人工发起退款
            log.error("【支付事故-需人工处理】订单已超时关闭(status=9)但微信已完成扣款，" +
                    "请登录微信商户平台发起退款！uuid={}, transactionId={}, amount(元)={}",
                    uuid, param.get("transactionId"), param.get("realPay"));
            return -1;
        }
        log.warn("微信支付回调：订单状态异常，不在预期范围，uuid={}, currentStatus={}", uuid, currentStatus);
        return 0;
    }

}
