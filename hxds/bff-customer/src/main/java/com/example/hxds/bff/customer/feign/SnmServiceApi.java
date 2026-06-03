package com.example.hxds.bff.customer.feign;

import com.example.hxds.bff.customer.controller.form.DeleteMessageRefByIdForm;
import com.example.hxds.bff.customer.controller.form.ReceiveBillMessageForm;
import com.example.hxds.bff.customer.controller.form.SearchMessageByIdForm;
import com.example.hxds.bff.customer.controller.form.SearchMessageByPageForm;
import com.example.hxds.bff.customer.controller.form.SendNewOrderMessageForm;
import com.example.hxds.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "hxds-snm")
public interface SnmServiceApi {
    @PostMapping("/message/order/new/sendNewOrderMessageAsync")
    R sendNewOrderMessageAsync(@RequestBody SendNewOrderMessageForm form);

    @PostMapping("/message/receiveBillMessage")
    R receiveBillMessage(@RequestBody ReceiveBillMessageForm form);

    @PostMapping("/message/searchMessageByPage")
    R searchMessageByPage(@RequestBody SearchMessageByPageForm form);

    @PostMapping("/message/searchMessageById")
    R searchMessageById(@RequestBody SearchMessageByIdForm form);

    @PostMapping("/message/updateUnreadMessage")
    R updateUnreadMessage(@RequestBody DeleteMessageRefByIdForm form);

    @PostMapping("/message/deleteMessageRefById")
    R deleteMessageRefById(@RequestBody DeleteMessageRefByIdForm form);
}
