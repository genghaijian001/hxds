package com.example.hxds.snm.controller;

import com.example.hxds.common.util.R;
import com.example.hxds.snm.controller.form.ClearNewOrderQueueForm;
import com.example.hxds.snm.controller.form.DeleteNewOrderQueueForm;
import com.example.hxds.snm.controller.form.ReceiveNewOrderMessageForm;
import com.example.hxds.snm.controller.form.SendNewOrderMessageForm;
import com.example.hxds.snm.entity.NewOrderMessage;
import com.example.hxds.snm.task.NewOrderMassageTask;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/message/order/new")
@Tag(name = "NewOrderMessageController", description = "新订单消息Web接口")
public class NewOrderMessageController {

    @Resource
    private NewOrderMassageTask task;

    @PostMapping("/sendNewOrderMessage")
    @Operation(summary = "同步发送新订单消息")
    public R sendNewOrderMessage(@RequestBody @Valid SendNewOrderMessageForm form){
        ArrayList<NewOrderMessage> list=new ArrayList<>();
        String[] driversContent= form.getDriversContent();
        for (String one : driversContent){
            String[] temp=one.split("#"); //司机ID#接单距离
            String userId=temp[0];
            String distance=temp[1];

            NewOrderMessage message=new NewOrderMessage();
            message.setUserId(userId);
            message.setOrderId(form.getOrderId().toString());
            message.setFrom(form.getFrom());
            message.setTo(form.getTo());
            message.setMileage(form.getMileage());
            message.setMinute(form.getMinute().toString());
            message.setDistance(distance);   //范围
            message.setExpectsFee(form.getExpectsFee());  //预估价格
            message.setFavourFee(form.getFavourFee()); //好处费

            list.add(message);
        }
        task.sendNewOrderMessage(list);
        return R.ok();
    }

    @PostMapping("/sendNewOrderMessageAsync")
    @Operation(summary = "异步发送新订单消息")
    public R sendNewOrderMessageAsync(@RequestBody @Valid SendNewOrderMessageForm form){
        ArrayList<NewOrderMessage> list=new ArrayList<>();
        String[] driversContent=form.getDriversContent();
        for(String one : driversContent){
            String[] temp=one.split("#");//司机ID#接单距离
            String userId=temp[0];
            String distance=temp[1];

            NewOrderMessage message=new NewOrderMessage();
            message.setUserId(userId);
            message.setOrderId(form.getOrderId().toString());
            message.setFrom(form.getFrom());
            message.setTo(form.getTo());
            message.setMileage(form.getMileage());
            message.setMinute(form.getMinute().toString());
            message.setDistance(distance);   //范围
            message.setExpectsFee(form.getExpectsFee());  //预估价格
            message.setFavourFee(form.getFavourFee()); //好处费

            list.add(message);
        }
        task.sendNewOrderMessageAsync(list);
        return R.ok();
    }

    @PostMapping("/receiveNewOrderMessage")
    @Operation(summary = "同步接收新订单消息")
    public R receiveNewOrderMessage(@RequestBody @Valid ReceiveNewOrderMessageForm form){
        List<NewOrderMessage> list=task.receiveNewOrderMessage(form.getUserId());
        return R.ok().put("result",list);
    }


    @PostMapping("/deleteNewOrderQueue")
    @Operation(summary = "同步删除新订单消息队列")
    public R deleteNewOrderQueue(@RequestBody @Valid DeleteNewOrderQueueForm form) {
        task.deleteNewOrderQueue(form.getUserId());
        return R.ok();
    }

    @PostMapping("/deleteNewOrderQueueeAsync")
    @Operation(summary = "异步删除新订单消息队列")
    public R deleteNewOrderQueueeAsync(@RequestBody @Valid DeleteNewOrderQueueForm form) {
        task.deleteNewOrderQueueAsync(form.getUserId());
        return R.ok();
    }

    @PostMapping("/clearNewOrderQueue")
    @Operation(summary = "同步清空新订单消息队列")
    public R clearNewOrderQueue(@RequestBody @Valid ClearNewOrderQueueForm form) {
        task.clearNewOrderQueue(form.getUserId());
        return R.ok();
    }

    @PostMapping("/clearNewOrderQueueAsync")
    @Operation(summary = "异步清空新订单消息队列")
    public R clearNewOrderQueueAsync(@RequestBody @Valid ClearNewOrderQueueForm form) {
        task.clearNewOrderQueueAsync(form.getUserId());
        return R.ok();
    }
}
