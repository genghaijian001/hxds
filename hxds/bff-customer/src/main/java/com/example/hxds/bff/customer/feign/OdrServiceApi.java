package com.example.hxds.bff.customer.feign;

import cn.hutool.core.bean.BeanUtil;
import com.example.hxds.bff.customer.controller.form.*;
import com.example.hxds.common.util.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@FeignClient(value = "hxds-odr")
public interface OdrServiceApi {

    @PostMapping("/order/insertOrder")
    public R insertOrder(InsertOrderForm form);

    @PostMapping("/order/searchOrderStatus")
    public R searchOrderStatus(SearchOrderStatusForm form);

    @PostMapping("/order/deleteUnAcceptOrder")
    public R deleteUnAcceptOrder(DeleteUnAcceptOrderForm form);

    @PostMapping("/order/hasCustomerCurrentOrder")
    public R hasCustomerCurrentOrder(HasCustomerCurrentOrderForm form);

    @PostMapping("/order/searchOrderForMoveById")
    public R searchOrderForMoveById(SearchOrderForMoveByIdForm form);

    @PostMapping("/order/confirmArriveStartPlace")
    public R confirmArriveStartPlace(ConfirmArriveStartPlaceForm form);

    @PostMapping("/order/searchOrderById")
    public R searchOrderById(SearchOrderByIdForm form);

    @PostMapping("/order/validCanPayOrder")
    @Operation(summary = "查询订单是否可以支付")
    public R validCanPayOrder(ValidCanPayOrderForm form);

    @PostMapping("/order/updateOrderPrepayId")
    @Operation(summary = "更新预支付订单ID")
    public R updateOrderPrepayId(UpdateOrderPrepayIdForm form);

    @PostMapping("/order/bill/updateBillPayment")
    @Operation(summary = "更新账单表实际支付")
    public R updateBillPayment(UpdateBillPaymentForm form);

    @PostMapping("/comment/insertComment")
    @Operation(summary = "保存订单评价")
    public R insertComment(InsertCommentForm form);

    @PostMapping("/order/searchCustomerOrderByPage")
    @Operation(summary = "小程序乘客查询订单评价详情分页")
    public R searchCustomerOrderByPage(SearchCustomerOrderByPageForm form);

    @PostMapping("/comment/searchCommentByOrderId")
    @Operation(summary = "保存订单评价")
    public R searchCommentByOrderId(SearchCommentByOrderIdForm form);
}
