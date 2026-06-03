package com.example.hxds.bff.driver.feign;

import com.example.hxds.bff.driver.controller.form.ClearNewOrderQueueForm;
import com.example.hxds.bff.driver.controller.form.DeleteMessageRefByIdForm;
import com.example.hxds.bff.driver.controller.form.ReceiveNewOrderMessageForm;
import com.example.hxds.bff.driver.controller.form.SearchMessageByIdForm;
import com.example.hxds.bff.driver.controller.form.SearchMessageByPageForm;
import com.example.hxds.bff.driver.controller.form.SendPrivateMessageForm;
import com.example.hxds.common.util.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@FeignClient(value = "hxds-snm")
public interface SnmServiceApi {

    @PostMapping("/message/order/new/clearNewOrderQueue")
    public R clearNewOrderQueue(@RequestBody ClearNewOrderQueueForm form);

    @PostMapping("/message/order/new/receiveNewOrderMessage")
    public R receiveNewOrderMessage(@RequestBody ReceiveNewOrderMessageForm form);

    @PostMapping("/message/sendPrivateMessage")
    @Operation(summary = "同步发送私有消息")
    public R sendPrivateMessage(@RequestBody SendPrivateMessageForm form);

    @PostMapping("/message/sendPrivateMessageSync")
    @Operation(summary = "异步发送私有消息")
    public R sendPrivateMessageSync(@RequestBody SendPrivateMessageForm form);

    @PostMapping("/message/searchMessageByPage")
    public R searchMessageByPage(@RequestBody SearchMessageByPageForm form);

    @PostMapping("/message/searchMessageById")
    public R searchMessageById(@RequestBody SearchMessageByIdForm form);

    @PostMapping("/message/deleteMessageRefById")
    public R deleteMessageRefById(@RequestBody DeleteMessageRefByIdForm form);
}
