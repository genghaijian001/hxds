package com.example.hxds.bff.customer.service;

import com.example.hxds.bff.customer.controller.form.*;
import com.example.hxds.common.util.PageUtils;
import com.example.hxds.common.util.R;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;

public interface OrderService {
    public HashMap createNewOrder(CreateNewOrderForm form);

    public Integer searchOrderStatus(SearchOrderStatusForm form);

    public String deleteUnAcceptOrder(DeleteUnAcceptOrderForm form);
    public HashMap hasCustomerCurrentOrder(HasCustomerCurrentOrderForm form);

    public HashMap searchOrderForMoveById(SearchOrderForMoveByIdForm form);
    public boolean confirmArriveStartPlace(ConfirmArriveStartPlaceForm form);

    public HashMap searchOrderById(SearchOrderByIdForm form);

    //创建支付订单
    public HashMap createWxPayment(long orderId, long customerId);

    //支付完成后更新订单状态
    public String updateOrderAboutPayment(UpdateOrderAboutPaymentForm form);

    public PageUtils searchCustomerOrderByPage(SearchCustomerOrderByPageForm form);


}
