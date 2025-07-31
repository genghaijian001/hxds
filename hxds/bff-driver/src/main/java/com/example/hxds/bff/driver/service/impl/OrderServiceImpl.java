package com.example.hxds.bff.driver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.example.hxds.bff.driver.controller.form.*;
import com.example.hxds.bff.driver.feign.*;
import com.example.hxds.bff.driver.service.OrderService;
import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.common.util.PageUtils;
import com.example.hxds.common.util.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.RoundingMode;
import java.util.HashMap;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OdrServiceApi odrServiceApi;

    @Resource
    private CstServiceApi cstServiceApi;

    @Resource
    private NebulaServiceApi nebulaServiceApi;

    @Resource
    private RuleServiceApi ruleServiceApi;

    @Resource
    private SnmServiceApi snmServiceApi;

    @Override
    @Transactional
    @LcnTransaction
    public String acceptNewOrder(AcceptNewOrderForm form) {
        R r = odrServiceApi.acceptNewOrder(form);
        String result = (String) r.get("result");
        return result;
    }

    @Override
    public HashMap searchDriverExecuteOrder(SearchDriverExecuteOrderForm form) {
        //查询订单信息
        R r = odrServiceApi.searchDriverExecuteOrder(form);
        HashMap orderMap= (HashMap) r.get("result");

        //查询代驾客户信息
        long customerId = MapUtil.getLong(orderMap, "customerId");
        SearchCustomerInfoInOrderForm infoInOrderForm=new SearchCustomerInfoInOrderForm();
        infoInOrderForm.setCustomerId(customerId);
        r= cstServiceApi.searchCustomerInfoInOrder(infoInOrderForm);
        HashMap cstMap= (HashMap) r.get("result");

        HashMap map=new HashMap();
        map.putAll(orderMap);
        map.putAll(cstMap);
        return map;
    }

    @Override
    public HashMap searchDriverCurrentOrder(SearchDriverCurrentOrderForm form) {
        R r = odrServiceApi.searchDriverCurrentOrder(form);
        HashMap orderMap= (HashMap) r.get("result");

        if (MapUtil.isNotEmpty(orderMap)) {
            HashMap map=new HashMap();
            long customerId = MapUtil.getLong(orderMap, "customerId");

            SearchCustomerInfoInOrderForm infoInOrderForm = new SearchCustomerInfoInOrderForm();
            infoInOrderForm.setCustomerId(customerId);
            r = cstServiceApi.searchCustomerInfoInOrder(infoInOrderForm);
            HashMap cstMap= (HashMap) r.get("result");

            map.putAll(orderMap);
            map.putAll(cstMap);
            return map;
        }else {
            return null;
        }
    }

    @Override
    public HashMap searchOrderForMoveById(SearchOrderForMoveByIdForm form) {
        R r = odrServiceApi.searchOrderForMoveById(form);
        HashMap map= (HashMap) r.get("result");
        return map;
    }

    @Override
    @Transactional
    @LcnTransaction
    public int arriveStartPlace(ArriveStartPlaceForm form) {
        R r = odrServiceApi.arriveStartPlace(form);
        int rows = MapUtil.getInt(r, "rows");
        if (rows == 1){
            //发送通知消息
        }
        return rows;
    }

    @Override
    @Transactional
    @LcnTransaction
    public int startDriving(StartDrivingForm form) {
        R r = odrServiceApi.startDriving(form);
        int rows = MapUtil.getInt(r, "rows");
        InsertOrderMonitoringForm monitoringForm=new InsertOrderMonitoringForm();
        monitoringForm.setOrderId(form.getOrderId());
        nebulaServiceApi.insertOrderMonitoring(monitoringForm);
        //发送通知消息

        return rows;
    }

    @Override
    @Transactional
    @LcnTransaction
    public int updateOrderStatus(UpdateOrderStatusForm form) {
        R r = odrServiceApi.updateOrderStatus(form);
        int rows=MapUtil.getInt(r,"rows");
        if (rows != 1){
            throw new HxdsException("订单状态修改失败");
        }
        /**
         * MQ发送同步私有消息
         */
        if (form.getStatus()==6){
            SendPrivateMessageForm message = new SendPrivateMessageForm();
            message.setReceiverIdentity("customer_bill");//接收者交换机
            message.setReceiverId(form.getCustomerId());//接收者用户ID
            message.setTtl(3*24*3600*1000);//3000天
            message.setSenderId(0L);//发送者 0代表系统发出
            message.setSenderIdentity("system");//发送者交换机
            message.setSenderName("HXDS-华星");
            message.setMsg("您有代驾订单待支付");
            snmServiceApi.sendPrivateMessageSync(message);
        }

        return rows;
    }

    @Override
    @Transactional
    @LcnTransaction
    public int updateBillFee(UpdateBillFeeForm form) {
        /**
         * 1.判断司机是否关联该订单
         */
        ValidDriverOwnOrderForm form_1 = new ValidDriverOwnOrderForm();
        form_1.setOrderId(form.getOrderId());
        form_1.setDriverId(form.getDriverId());
        R r = odrServiceApi.validDriverOwnOrder(form_1);
        boolean bool = MapUtil.getBool(r,"result");
        if (!bool){
            throw new HxdsException("司机未关联该订单");
        }

        /**
         * 2.计算订单里程数据
         */
        CalculateOrderMileageForm form_2 = new CalculateOrderMileageForm();
        form_2.setOrderId(form.getOrderId());
        r=nebulaServiceApi.calculateOrderMileage(form_2);
        String mileage = (String) r.get("result");
        mileage = NumberUtil.div(mileage,"1000",1, RoundingMode.CEILING).toString();

        /**
         * 3.查询订单消息
         */
        SearchSettlementNeedDataForm form_3 = new SearchSettlementNeedDataForm();
        form_3.setOrderId(form.getOrderId());
        r=odrServiceApi.searchSettlementNeedData(form_3);
        HashMap map = (HashMap) r.get("result");
        String acceptTime = MapUtil.getStr(map, "acceptTime");//司机接单时间
        String startTime = MapUtil.getStr(map, "startTime");//代驾开始时间
        int waitingMinute = MapUtil.getInt(map, "waitingMinute");//代驾等时分钟数
        String favourFee = MapUtil.getStr(map, "favourFee");//好处费

        /**
         * 4.计算代驾费
         */
        CalculateOrderChargeForm form_4 = new CalculateOrderChargeForm();
        form_4.setMileage(mileage);
        form_4.setTime(startTime.split(" ")[1]);
        form_4.setMinute(waitingMinute);
        r=ruleServiceApi.calculateOrderCharge(form_4);
        map = (HashMap) r.get("result");
        String mileageFee = MapUtil.getStr(map, "mileageFee");
        String returnFee = MapUtil.getStr(map, "returnFee");
        String waitingFee = MapUtil.getStr(map, "waitingFee");
        String amount = MapUtil.getStr(map, "amount");
        String returnMileage = MapUtil.getStr(map, "returnMileage");

        /**
         * 5.计算系统奖励费用
         */
        CalculateIncentiveFeeForm form_5 = new CalculateIncentiveFeeForm();
        form_5.setDriverId(form.getDriverId());
        form_5.setAcceptTime(acceptTime);
//        r=ruleServiceApi.calculateIncentiveFee(form_5);
//        String incentiveFee = (String) r.get("result");

        form.setMileageFee(mileageFee);//里程费
        form.setReturnFee(returnFee);//返程费
        form.setWaitingFee(waitingFee);//等时费
       // form.setIncentiveFee(incentiveFee);
        form.setRealMileage(mileage);//里程
        form.setReturnMileage(returnMileage);//返程里程

        //计算总费用
        String total = NumberUtil.add(amount, form.getTollFee(), form.getParkingFee(), form.getOtherFee(), favourFee).toString();
        form.setTotal(total);

        /**
         * 6.计算分账数据
         */
        CalculateProfitsharingForm form_6 = new CalculateProfitsharingForm();
        form_6.setOrderId(form.getOrderId());
        form_6.setAmount(total);
        r=ruleServiceApi.calculateProfitsharing(form_6);
        map= (HashMap) r.get("result");
        long ruleId = MapUtil.getLong(map, "ruleId");
        String systemIncome = MapUtil.getStr(map, "systemIncome");//系统分账收入
        String driverIncome = MapUtil.getStr(map, "driverIncome");//司机分账收入（扣除个税）
        String paymentRate = MapUtil.getStr(map, "paymentRate");//微信支付接口渠道费率
        String paymentFee = MapUtil.getStr(map, "paymentFee");//微信支付接口渠道费
        String taxRate = MapUtil.getStr(map, "taxRate");//替司机代缴个税的税率（19%）
        String taxFee = MapUtil.getStr(map, "taxFee");//替司机代缴个税金额

        form.setRuleId(ruleId);
        form.setPaymentRate(paymentRate);
        form.setPaymentFee(paymentFee);
        form.setTaxRate(taxRate);
        form.setTaxFee(taxFee);
        form.setSystemIncome(systemIncome);
        form.setDriverIncome(driverIncome);

        /**
         * 7.更新代驾费账单数据
         */
        r=odrServiceApi.updateBillFee(form);
        int rows = MapUtil.getInt(r,"rows");
        return rows;
    }

    @Override
    public HashMap searchReviewDriverOrderBill(SearchReviewDriverOrderBillForm form) {
        R r = odrServiceApi.searchReviewDriverOrderBill(form);
        HashMap result = (HashMap) r.get("result");
        return result;
    }

    @Override
    public PageUtils searchDriverOrderByPage(SearchDriverOrderByPageForm form) {
        R r = odrServiceApi.searchDriverOrderByPage(form);
        HashMap result = (HashMap) r.get("result");
        PageUtils pageUtils = BeanUtil.toBean(result,PageUtils.class);
        return pageUtils;
    }

    @Override
    public HashMap searchOrderById(SearchOrderByIdForm form) {
        //查询订单信息
        R r = odrServiceApi.searchOrderById(form);
        HashMap orderMap= (HashMap) r.get("result");

        //查询代驾客户信息
        long customerId = MapUtil.getLong(orderMap, "customerId");
        SearchCustomerInfoInOrderForm infoInOrderForm=new SearchCustomerInfoInOrderForm();
        infoInOrderForm.setCustomerId(customerId);
        r= cstServiceApi.searchCustomerInfoInOrder(infoInOrderForm);
        HashMap cstMap= (HashMap) r.get("result");

        //查询订单评价信息
        int status = MapUtil.getInt(orderMap, "status");
        HashMap cmtMap=new HashMap();
        if(status>=7){
            SearchCommentByOrderIdForm form_2 = new SearchCommentByOrderIdForm();
            form_2.setOrderId(form.getOrderId());
            form_2.setDriverId(form.getDriverId());
            r = odrServiceApi.searchCommentByOrderId(form_2);

            if (r.containsKey("result")){
                cmtMap= (HashMap) r.get("result");
            }else{
                cmtMap.put("rate",5);
            }

        }

        HashMap map=new HashMap();
        map.putAll(orderMap);
        map.putAll(cstMap);
        map.put("comment",cmtMap);
        return map;
    }


}
