package com.example.hxds.bff.customer.feign;

import cn.hutool.core.bean.BeanUtil;
import com.example.hxds.bff.customer.controller.form.*;
import com.example.hxds.common.util.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@FeignClient(value = "hxds-odr")
public interface OdrServiceApi {

    @PostMapping("/order/insertOrder")
    public R insertOrder(@RequestBody InsertOrderForm form);

    @PostMapping("/order/searchOrderStatus")
    public R searchOrderStatus(@RequestBody SearchOrderStatusForm form);

    @PostMapping("/order/deleteUnAcceptOrder")
    public R deleteUnAcceptOrder(@RequestBody DeleteUnAcceptOrderForm form);

    @PostMapping("/order/hasCustomerCurrentOrder")
    public R hasCustomerCurrentOrder(@RequestBody HasCustomerCurrentOrderForm form);

    @PostMapping("/order/searchOrderForMoveById")
    public R searchOrderForMoveById(@RequestBody SearchOrderForMoveByIdForm form);

    @PostMapping("/order/confirmArriveStartPlace")
    public R confirmArriveStartPlace(@RequestBody ConfirmArriveStartPlaceForm form);

    @PostMapping("/order/searchOrderById")
    public R searchOrderById(@RequestBody SearchOrderByIdForm form);

    @PostMapping("/order/validCanPayOrder")
    @Operation(summary = "查询订单是否可以支付")
    public R validCanPayOrder(@RequestBody ValidCanPayOrderForm form);

    @PostMapping("/order/updateOrderPrepayId")
    @Operation(summary = "更新预支付订单ID")
    public R updateOrderPrepayId(@RequestBody UpdateOrderPrepayIdForm form);

    @PostMapping("/bill/updateBillPayment")
    @Operation(summary = "更新账单表实际支付")
    public R updateBillPayment(@RequestBody UpdateBillPaymentForm form);

    @PostMapping("/comment/insertComment")
    @Operation(summary = "保存订单评价")
    public R insertComment(@RequestBody InsertCommentForm form);

    @PostMapping("/order/searchCustomerOrderByPage")
    @Operation(summary = "小程序乘客查询订单评价详情分页")
    public R searchCustomerOrderByPage(@RequestBody SearchCustomerOrderByPageForm form);

    @PostMapping("/comment/searchCommentByOrderId")
    @Operation(summary = "保存订单评价")
    public R searchCommentByOrderId(@RequestBody SearchCommentByOrderIdForm form);

    @PostMapping("/order/updateOrderPayStatus")
    @Operation(summary = "司机端手动确认付款后更新订单状态为已付款")
    public R updateOrderPayStatus(@RequestBody UpdateOrderPayStatusForm form);
}
