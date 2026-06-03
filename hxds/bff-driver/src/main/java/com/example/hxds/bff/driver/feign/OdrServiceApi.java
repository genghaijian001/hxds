package com.example.hxds.bff.driver.feign;

import com.example.hxds.bff.driver.controller.form.*;
import com.example.hxds.bff.driver.controller.form.UpdateBillPaymentForm;
import com.example.hxds.common.util.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@FeignClient(value = "hxds-odr")
public interface OdrServiceApi {

    @PostMapping("/order/searchDriverTodayBusinessData")
    public R searchDriverTodayBusinessData(@RequestBody SearchDriverTodayBusinessDataForm form);

    @PostMapping("/order/acceptNewOrder")
    public R acceptNewOrder(@RequestBody AcceptNewOrderForm form);

    @PostMapping("/order/searchDriverExecuteOrder")
    public R searchDriverExecuteOrder(@RequestBody SearchDriverExecuteOrderForm form);

    @PostMapping("/order/searchDriverCurrentOrder")
    public R searchDriverCurrentOrder(@RequestBody SearchDriverCurrentOrderForm form);

    @PostMapping("/order/searchOrderForMoveById")
    public R searchOrderForMoveById(@RequestBody SearchOrderForMoveByIdForm form);

    @PostMapping("/order/arriveStartPlace")
    public R arriveStartPlace(@RequestBody ArriveStartPlaceForm form);

    @PostMapping("/order/startDriving")
    public R startDriving(@RequestBody StartDrivingForm form);

    @PostMapping("/order/updateOrderStatus")
    public R updateOrderStatus(@RequestBody UpdateOrderStatusForm form);

    @PostMapping("/order/validDriverOwnOrder")
    public R validDriverOwnOrder(@RequestBody ValidDriverOwnOrderForm form);

    @PostMapping("/order/searchSettlementNeedData")
    public R searchSettlementNeedData(@RequestBody SearchSettlementNeedDataForm form);

    @PostMapping("/bill/updateBillFee")
    public R updateBillFee(@RequestBody UpdateBillFeeForm form);

    @PostMapping("/bill/searchReviewDriverOrderBill")
    public R searchReviewDriverOrderBill(@RequestBody SearchReviewDriverOrderBillForm form);

    @PostMapping("/order/searchDriverOrderByPage")
    @Operation(summary = "小程序司机查询订单评价详情分页")
    public R searchDriverOrderByPage(@RequestBody SearchDriverOrderByPageForm form);

    @PostMapping("/order/searchOrderById")
    public R searchOrderById(@RequestBody SearchOrderByIdForm form);

    @PostMapping("/comment/searchCommentByOrderId")
    @Operation(summary = "保存订单评价")
    public R searchCommentByOrderId(@RequestBody SearchCommentByOrderIdForm form);

    @PostMapping("/order/searchOrderStatus")
    public R searchOrderStatus(@RequestBody SearchOrderStatusForm form);

    @PostMapping("/bill/updateBillPayment")
    @Operation(summary = "更新账单表实际支付")
    public R updateBillPayment(@RequestBody UpdateBillPaymentForm form);

}
