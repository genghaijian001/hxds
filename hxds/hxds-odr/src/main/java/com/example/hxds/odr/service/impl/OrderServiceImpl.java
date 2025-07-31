package com.example.hxds.odr.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
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

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
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
    @LcnTransaction
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
    @LcnTransaction
    public String acceptNewOrder(long driverId, long orderId) {
        if (!redisTemplate.hasKey("order#" + orderId)){
            return "抢单失败";
        }
        //执行redis事务
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //获取新订单记录的Version
                operations.watch("order#" + orderId);
                //本地缓存Redis操作
                operations.multi();
                //把新订单缓存的Value设置成抢单司机的ID
                operations.opsForValue().set("order#" + orderId,driverId);
                //执行Redis事务，如果事务提交失败会自动抛出异常
                return operations.exec();
            }
        });
        //抢单成功之后，删除Redis中的新订单，避免让其他司机参与抢单
        redisTemplate.delete("order#" + orderId);
        //更新订单记录，添加上接单司机ID和接单时间
        HashMap param=new HashMap(){{
            put("driverId" , driverId);
            put("orderId" , orderId);
        }};
        int rows = orderDao.acceptNewOrder(param);
        if (rows != 1){
            throw new HxdsException("接单失败，无法更新订单记录");
        }
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
    @LcnTransaction
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
    @LcnTransaction
    public int arriveStartPlace(Map param) {
        //添加到达上车点标志位
        long orderId = MapUtil.getLong(param, "orderId");
        redisTemplate.opsForValue().set("order_driver_arrivied#"+orderId,"1");
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
        // 1等待接单，2已接单，3司机已到达，4开始代驾，5结束代驾，6未付款，7已付款，
        // 8订单已结束，9顾客撤单，10司机撤单，11事故关闭，12其他
        if (redisTemplate.hasKey(key) && redisTemplate.opsForValue().get(key).toString().equals("1")){
            redisTemplate.opsForValue().set(key,"2");
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
    @LcnTransaction
    public int startDriving(Map param) {
        long orderId = MapUtil.getLong(param, "orderId");
        String key="order_driver_arrivied#"+orderId;
//        if(redisTemplate.hasKey(key) && redisTemplate.opsForValue().get(key).toString().equals("2")){
//            redisTemplate.delete(key);
            int rows = orderDao.updateOrderStatus(param);
            if (rows != 1){
                throw new HxdsException("更新订单状态失败");
            }
            return rows;
//        }
    }

    /**
     * 结束代驾  更新订单状态
     * @param param
     * @return
     */
    @Override
    @Transactional
    @LcnTransaction
    public int updateOrderStatus(Map param) {
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
    @LcnTransaction
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

}
