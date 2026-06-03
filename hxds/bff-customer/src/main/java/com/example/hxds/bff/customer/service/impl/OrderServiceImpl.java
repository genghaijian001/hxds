package com.example.hxds.bff.customer.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import io.seata.spring.annotation.GlobalTransactional;
import com.example.hxds.bff.customer.controller.form.*;
import com.example.hxds.bff.customer.feign.*;
import com.example.hxds.bff.customer.service.OrderService;
import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.common.util.PageUtils;
import com.example.hxds.common.util.R;
import com.example.hxds.common.wxpay.MyWXPayConfig;
import com.example.hxds.common.wxpay.WXPay;
import com.example.hxds.common.wxpay.WXPayUtil;
import com.wechat.pay.java.core.RSAPublicKeyConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.jsapi.model.QueryOrderByOutTradeNoRequest;
import com.wechat.pay.java.service.payments.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Value("${wx.notify-url}")
    private String wxPayNotifyUrl;

    @Resource
    private MpsServiceApi mpsServiceApi;

    @Resource
    private RuleServiceApi ruleServiceApi;

    @Resource
    private OdrServiceApi odrServiceApi;

    @Resource
    private SnmServiceApi snmServiceApi;

    @Resource
    private DrServiceApi drServiceApi;

    @Resource
    private CstServiceApi cstServiceApi;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private MyWXPayConfig myWXPayConfig;

    @Resource
    private RSAPublicKeyConfig rsaPublicKeyConfig;

    @Value("${wx.v3.mch-id}")
    private String wxV3MchId;

    @Override
    @GlobalTransactional
    public HashMap createNewOrder(CreateNewOrderForm form) {
        Long customerId = form.getCustomerId();
        String startPlace = form.getStartPlace(); //订单起点
        String startPlaceLatitude = form.getStartPlaceLatitude();
        String startPlaceLongitude = form.getStartPlaceLongitude();
        String endPlace = form.getEndPlace();
        String endPlaceLatitude = form.getEndPlaceLatitude();
        String endPlaceLongitude = form.getEndPlaceLongitude();
        String favourFee = form.getFavourFee();  //顾客好处费

        // Fix-9: 幂等键防止重复下单（30秒内同一乘客同一起点不能重复提交）
        String idempotencyKey = "create_order#" + customerId + "#" + startPlaceLatitude + "#" + startPlaceLongitude;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(idempotencyKey))) {
            throw new HxdsException("您的订单正在处理中，请勿重复提交");
        }
        redisTemplate.opsForValue().set(idempotencyKey, "1", 30, TimeUnit.SECONDS);

        /**
         *
         [重新预估里程和时间]
         *虽然下单前，系统会预估里程和时长，但是有可能顾客在下单页面停留时间过长，
         *然后再按下单键，这时候路线和时长可能都有变化，所以需要重新预估里程和时间
         */
        EstimateOrderMileageAndMinuteForm form_1 = new EstimateOrderMileageAndMinuteForm();
        form_1.setMode("driving");
        form_1.setStartPlaceLatitude(startPlaceLatitude);
        form_1.setStartPlaceLongitude(startPlaceLongitude);
        form_1.setEndPlaceLatitude(endPlaceLatitude);
        form_1.setEndPlaceLongitude(endPlaceLongitude);
        R r = mpsServiceApi.estimateOrderMileageAndMinute(form_1);
        HashMap map= (HashMap) r.get("result");
        String mileage = MapUtil.getStr(map, "mileage");
        int minute = MapUtil.getInt(map, "minute");

        /**
         * 重新估算订单金额
         */
        EstimateOrderChargeForm form_2 = new EstimateOrderChargeForm();
        form_2.setMileage(mileage);
        form_2.setTime(new DateTime().toTimeStr());
        r = ruleServiceApi.estimateOrderCharge(form_2);
        map = (HashMap) r.get("result");
        String expectsFee = MapUtil.getStr(map, "amount");
        String chargeRuleId = MapUtil.getStr(map, "chargeRuleId");
        short baseMileage = MapUtil.getShort(map, "baseMileage");
        String baseMileagePrice = MapUtil.getStr(map, "baseMileagePrice");
        String exceedMileagePrice = MapUtil.getStr(map, "exceedMileagePrice");
        short baseMinute = MapUtil.getShort(map, "baseMinute");
        String exceedMinutePrice = MapUtil.getStr(map, "exceedMinutePrice");
        short baseReturnMileage = MapUtil.getShort(map, "baseReturnMileage");
        String exceedReturnPrice = MapUtil.getStr(map, "exceedReturnPrice");

        /**
         * 搜索适合接单的司机
         */
        SearchBefittingDriverAboutOrderForm form_3=new SearchBefittingDriverAboutOrderForm();
        form_3.setStartPlaceLatitude(startPlaceLatitude);
        form_3.setStartPlaceLongitude(startPlaceLongitude);
        form_3.setEndPlaceLatitude(endPlaceLatitude);
        form_3.setEndPlaceLongitude(endPlaceLongitude);
        form_3.setMileage(mileage);

        r=mpsServiceApi.searchBefittingDriverAboutOrder(form_3);
        ArrayList<HashMap> list = (ArrayList<HashMap>) r.get("result");
        HashMap result=new HashMap(){{
            put("count",0);
        }};

        //如果存在适合接单的司机就创建订单，否则就不创建订单
        if (list.size()>0){
            /**
             * 生成订单记录
             */
            InsertOrderForm form_4 = new InsertOrderForm();
            //UUID字符串，充当订单号，微信支付时候会用上
            form_4.setUuid(IdUtil.simpleUUID());
            form_4.setCustomerId(customerId);
            form_4.setStartPlace(startPlace);
            form_4.setStartPlaceLatitude(startPlaceLatitude);
            form_4.setStartPlaceLongitude(startPlaceLongitude);
            form_4.setEndPlace(endPlace);
            form_4.setEndPlaceLatitude(endPlaceLatitude);
            form_4.setEndPlaceLongitude(endPlaceLongitude);
            form_4.setExpectsMileage(mileage);
            form_4.setExpectsFee(expectsFee);
            form_4.setFavourFee(favourFee);
            form_4.setDate(new DateTime().toDateStr());
            form_4.setChargeRuleId(Long.parseLong(chargeRuleId));
            form_4.setCarPlate(form.getCarPlate());
            form_4.setCarType(form.getCarType());
            form_4.setBaseMileage(baseMileage);
            form_4.setBaseMileagePrice(baseMileagePrice);
            form_4.setExceedMileagePrice(exceedMileagePrice);
            form_4.setBaseMinute(baseMinute);
            form_4.setExceedMinutePrice(exceedMinutePrice);
            form_4.setBaseReturnMileage(baseReturnMileage);
            form_4.setExceedReturnPrice(exceedReturnPrice);

            r = odrServiceApi.insertOrder(form_4);
            String orderId = MapUtil.getStr(r, "result");

            //发送通知给符合条件的司机抢单
            SendNewOrderMessageForm form_5=new SendNewOrderMessageForm();
            String[] driverContent=new String[list.size()];
            for (int i=0;i< list.size();i++){
                HashMap one=list.get(i);
                String driverId = MapUtil.getStr(one, "driverId");
                String distance = MapUtil.getStr(one, "distance");
                                                           //小数点后一位
                distance=new BigDecimal(distance).setScale(1, RoundingMode.CEILING).toString();
                driverContent[i]=driverId+"#"+distance; //司机ID#接单距离
            }
            form_5.setDriversContent(driverContent);
            form_5.setOrderId(Long.parseLong(orderId));
            form_5.setFrom(startPlace);
            form_5.setTo(endPlace);
            form_5.setExpectsFee(expectsFee);
                                                    //里程转换保留小数点后一位
            mileage=new BigDecimal(mileage).setScale(1,RoundingMode.CEILING).toString();
            form_5.setMileage(mileage);
            form_5.setMinute(minute);
            form_5.setFavourFee(favourFee);
            snmServiceApi.sendNewOrderMessageAsync(form_5); //异步发送消息

            result.put("orderId", orderId);
            result.replace("count", list.size());
        }
        return result;
    }

    @Override
    public Integer searchOrderStatus(SearchOrderStatusForm form) {
        R r = odrServiceApi.searchOrderStatus(form);
        Integer status= MapUtil.getInt(r,"result");
        return status;
    }

    @Override
    @GlobalTransactional
    public String deleteUnAcceptOrder(DeleteUnAcceptOrderForm form) {
        R r = odrServiceApi.deleteUnAcceptOrder(form);
        String result = MapUtil.getStr(r, "result");
        return result;
    }

    @Override
    public HashMap hasCustomerCurrentOrder(HasCustomerCurrentOrderForm form) {
        R r = odrServiceApi.hasCustomerCurrentOrder(form);
        HashMap map= (HashMap) r.get("result");
        return map;
    }

    @Override
    public HashMap searchOrderForMoveById(SearchOrderForMoveByIdForm form) {
        R r = odrServiceApi.searchOrderForMoveById(form);
        HashMap result= (HashMap) r.get("result");
        return result;
    }

    @Override
    public boolean confirmArriveStartPlace(ConfirmArriveStartPlaceForm form) {
        R r = odrServiceApi.confirmArriveStartPlace(form);
        Boolean result = MapUtil.getBool(r, "result");
        return result;
    }

    @Override
    public HashMap searchOrderById(SearchOrderByIdForm form) {
        R r = odrServiceApi.searchOrderById(form);
        HashMap result= (HashMap) r.get("result");
        Long driverId = MapUtil.getLong(result, "driverId");
        if (driverId != null){
            SearchDriverBriefInfoForm form_2 = new SearchDriverBriefInfoForm();
            form_2.setDriverId(driverId);
            r=drServiceApi.searchDriverBriefInfo(form_2);

            HashMap temp = (HashMap) r.get("result");
            result.putAll(temp);
            //查询订单评价信息
            int status = MapUtil.getInt(result, "status");
            HashMap cmtMap=new HashMap();
            if(status>=7){
                SearchCommentByOrderIdForm commentFrom = new SearchCommentByOrderIdForm();
                commentFrom.setOrderId(form.getOrderId());
                commentFrom.setCustomerId(form.getCustomerId());
                r = odrServiceApi.searchCommentByOrderId(commentFrom);

                if (r.containsKey("result")){
                    cmtMap= (HashMap) r.get("result");
                }else{
                    cmtMap.put("rate",5);
                }
            }
            result.put("comment",cmtMap);
            return result;
        }
        return null;
    }

    @Override
    @GlobalTransactional
    public HashMap createWxPayment(long orderId, long customerId) {
        /**
         * 1.先查询订单是否为6状态，其他状态都不可以生成支付订单
         */
        ValidCanPayOrderForm form_1 = new ValidCanPayOrderForm();
        form_1.setOrderId(orderId);
        form_1.setCustomerId(customerId);
        R r = odrServiceApi.validCanPayOrder(form_1);
        HashMap map = (HashMap) r.get("result");
        String amount = MapUtil.getStr(map, "realFee");//实际金额
        String uuid = MapUtil.getStr(map, "uuid");
        Long driverId = MapUtil.getLong(map, "driverId");
        String discount="0.00";

        /**
         * 2.查询代金券是否可以使用，并绑定
         */

        /**
         * 3.修改实付金额
         */
        // API-1修复: 移除重复的 setOrderId，补充 setVoucherFee
        UpdateBillPaymentForm form_3 = new UpdateBillPaymentForm();
        form_3.setOrderId(orderId);
        form_3.setRealPay(amount);
        form_3.setVoucherFee(discount);

        /**
         * 4.查询用户的0penID字符串
         */
        SearchCustomerOpenIdForm form_4 = new SearchCustomerOpenIdForm();
        form_4.setCustomerId(customerId);
        r = cstServiceApi.searchCustomerOpenId(form_4);
        String customerOpenId = MapUtil.getStr(r, "result");

        /**
         * 5.查询司机的OpenId字符串
         */
        SearchDriverOpenIdForm form_5 = new SearchDriverOpenIdForm();
        form_5.setDriverId(driverId);
        r = drServiceApi.searchDriverOpenId(form_5);
        String driverOpenId = MapUtil.getStr(r, "result");

        /**
         * 6. 创建V3支付订单
         */
        try {
            JsapiServiceExtension service = new JsapiServiceExtension.Builder()
                    .config(rsaPublicKeyConfig)
                    .build();

            PrepayRequest prepayRequest = new PrepayRequest();
            prepayRequest.setAppid(myWXPayConfig.getAppID());
            prepayRequest.setMchid(wxV3MchId);
            prepayRequest.setDescription("代驾费");
            prepayRequest.setOutTradeNo(uuid);
            prepayRequest.setNotifyUrl(wxPayNotifyUrl);
            prepayRequest.setAttach(driverOpenId);

            Amount amountObj = new Amount();
            amountObj.setTotal(NumberUtil.mul(amount, "100").setScale(0, RoundingMode.HALF_UP).intValue());
            amountObj.setCurrency("CNY");
            prepayRequest.setAmount(amountObj);

            Payer payer = new Payer();
            payer.setOpenid(customerOpenId);
            prepayRequest.setPayer(payer);

            PrepayWithRequestPaymentResponse resp = service.prepayWithRequestPayment(prepayRequest);
            log.info("微信V3统一下单成功: packageVal={}", resp.getPackageVal());

            String packageVal = resp.getPackageVal(); // "prepay_id=xxx"
            String prepayId = packageVal.replace("prepay_id=", "");

            /**
             * 7.更新订单记录中的prepay_id字段值
             */
            UpdateOrderPrepayIdForm form_6 = new UpdateOrderPrepayIdForm();
            form_6.setOrderId(orderId);
            form_6.setPrepayId(prepayId);
            odrServiceApi.updateOrderPrepayId(form_6);

            odrServiceApi.updateBillPayment(form_3);

            map.clear();
            map.put("appId", resp.getAppId());
            map.put("timeStamp", resp.getTimeStamp());
            map.put("nonceStr", resp.getNonceStr());
            map.put("package", packageVal);
            map.put("signType", resp.getSignType());
            map.put("paySign", resp.getPaySign());
            map.put("uuid", uuid);
            return map;
        } catch (Exception e) {
            log.error("创建V3支付订单失败", e);
            throw new HxdsException("创建支付订单失败: " + e.getMessage());
        }
    }

    @Override
    public String updateOrderAboutPayment(UpdateOrderAboutPaymentForm form) {
        long orderId = form.getOrderId();
        long customerId = StpUtil.getLoginIdAsLong();

        // 先查当前状态，若回调已处理（状态>=7）直接返回成功
        SearchOrderStatusForm statusForm = new SearchOrderStatusForm();
        statusForm.setOrderId(orderId);
        statusForm.setCustomerId(customerId);
        R r = odrServiceApi.searchOrderStatus(statusForm);
        Integer status = MapUtil.getInt(r, "result");
        if (status != null && status >= 7) {
            return "付款成功";
        }

        // 回调尚未到达，用V3接口主动查询
        try {
            ValidCanPayOrderForm canPayForm = new ValidCanPayOrderForm();
            canPayForm.setOrderId(orderId);
            canPayForm.setCustomerId(customerId);
            r = odrServiceApi.validCanPayOrder(canPayForm);
            HashMap map = (HashMap) r.get("result");
            String uuid = MapUtil.getStr(map, "uuid");

            JsapiService service = new JsapiService.Builder()
                    .config(rsaPublicKeyConfig)
                    .build();

            QueryOrderByOutTradeNoRequest queryReq = new QueryOrderByOutTradeNoRequest();
            queryReq.setMchid(wxV3MchId);
            queryReq.setOutTradeNo(uuid);

            Transaction transaction = service.queryOrderByOutTradeNo(queryReq);
            if (transaction.getTradeState() == Transaction.TradeStateEnum.SUCCESS) {
                // 主动查询确认付款成功，更新DB状态为7，确保driver端轮询能感知
                String transactionId = transaction.getTransactionId();
                Integer payerTotal = transaction.getAmount().getPayerTotal();
                String realPay = new java.math.BigDecimal(payerTotal)
                        .divide(new java.math.BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP)
                        .toString();
                UpdateOrderPayStatusForm payStatusForm = new UpdateOrderPayStatusForm();
                payStatusForm.setUuid(uuid);
                payStatusForm.setTransactionId(transactionId);
                payStatusForm.setRealPay(realPay);
                try {
                    odrServiceApi.updateOrderPayStatus(payStatusForm);
                } catch (Exception ex) {
                    log.warn("更新订单支付状态失败(不阻断): {}", ex.getMessage());
                }
                return "付款成功";
            }
        } catch (Exception e) {
            log.warn("主动查询V3支付状态失败: {}", e.getMessage());
        }

        return "付款处理中，请稍候";
    }

    @Override
    public PageUtils searchCustomerOrderByPage(SearchCustomerOrderByPageForm form) {
        R r = odrServiceApi.searchCustomerOrderByPage(form);
        HashMap map = (HashMap) r.get("result");
        PageUtils pageUtils= BeanUtil.toBean(map,PageUtils.class);
        return pageUtils;
    }
}
