package com.example.hxds.bff.driver.feign;

import com.example.hxds.bff.driver.controller.form.*;
import com.example.hxds.common.util.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "hxds-odr")
public interface OdrServiceApi {

    @PostMapping("/order/searchDriverTodayBusinessData")
    public R searchDriverTodayBusinessData(SearchDriverTodayBusinessDataForm form);

    @PostMapping("/order/acceptNewOrder")
    public R acceptNewOrder(AcceptNewOrderForm form);

    @PostMapping("/order/searchDriverExecuteOrder")
    public R searchDriverExecuteOrder(SearchDriverExecuteOrderForm form);

    @PostMapping("/order/searchDriverCurrentOrder")
    public R searchDriverCurrentOrder(SearchDriverCurrentOrderForm form);

    @PostMapping("/order/searchOrderForMoveById")
    public R searchOrderForMoveById(SearchOrderForMoveByIdForm form);

    @PostMapping("/order/arriveStartPlace")
    public R arriveStartPlace(ArriveStartPlaceForm form);

    @PostMapping("/order/startDriving")
    public R startDriving(StartDrivingForm form);

    @PostMapping("/order/updateOrderStatus")
    public R updateOrderStatus(UpdateOrderStatusForm form);

    @PostMapping("/order/validDriverOwnOrder")
    public R validDriverOwnOrder(ValidDriverOwnOrderForm form);

    @PostMapping("/order/searchSettlementNeedData")
    public R searchSettlementNeedData(SearchSettlementNeedDataForm form);

    @PostMapping("/bill/updateBillFee")
    public R updateBillFee(UpdateBillFeeForm form);

    @PostMapping("/bill/searchReviewDriverOrderBill")
    public R searchReviewDriverOrderBill(SearchReviewDriverOrderBillForm form);

    @PostMapping("/order/searchDriverOrderByPage")
    @Operation(summary = "小程序司机查询订单评价详情分页")
    public R searchDriverOrderByPage(SearchDriverOrderByPageForm form);

    @PostMapping("/order/searchOrderById")
    public R searchOrderById(SearchOrderByIdForm form);

    @PostMapping("/comment/searchCommentByOrderId")
    @Operation(summary = "保存订单评价")
    public R searchCommentByOrderId(SearchCommentByOrderIdForm form);



}
