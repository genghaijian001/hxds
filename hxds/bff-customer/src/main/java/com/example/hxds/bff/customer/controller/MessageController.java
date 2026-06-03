package com.example.hxds.bff.customer.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.example.hxds.bff.customer.controller.form.DeleteMessageRefByIdForm;
import com.example.hxds.bff.customer.controller.form.ReceiveBillMessageForm;
import com.example.hxds.bff.customer.controller.form.SearchMessageByIdForm;
import com.example.hxds.bff.customer.controller.form.SearchMessageByPageForm;
import com.example.hxds.bff.customer.service.MessageService;
import com.example.hxds.common.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/message")
@Tag(name = "MessageController", description = "Message web API")
public class MessageController {
    @Resource
    private MessageService messageService;

    @PostMapping("/receiveBillMessage")
    @SaCheckLogin
    @Operation(summary = "Receive bill message")
    public R receiveBillMessage(@RequestBody @Valid ReceiveBillMessageForm form) {
        long customerId = StpUtil.getLoginIdAsLong();
        form.setUserId(customerId);
        form.setIdentity("customer_bill");
        String msg = messageService.receiveBillMessage(form);
        return R.ok().put("result", msg);
    }

    @PostMapping("/searchMessageByPage")
    @SaCheckLogin
    @Operation(summary = "Search message list")
    public R searchMessageByPage(@Valid @RequestBody SearchMessageByPageForm form) {
        long customerId = StpUtil.getLoginIdAsLong();
        form.setUserId(customerId);
        form.setIdentity("customer");
        ArrayList list = messageService.searchMessageByPage(form);
        return R.ok().put("result", list);
    }

    @PostMapping("/searchMessageById")
    @SaCheckLogin
    @Operation(summary = "Search message detail")
    public R searchMessageById(@Valid @RequestBody SearchMessageByIdForm form) {
        HashMap map = messageService.searchMessageById(form.getId());
        return R.ok().put("result", map);
    }

    @PostMapping("/updateUnreadMessage")
    @SaCheckLogin
    @Operation(summary = "Mark message as read")
    public R updateUnreadMessage(@Valid @RequestBody DeleteMessageRefByIdForm form) {
        long rows = messageService.updateUnreadMessage(form.getId());
        return R.ok().put("rows", rows);
    }

    @PostMapping("/deleteMessageRefById")
    @SaCheckLogin
    @Operation(summary = "Delete message")
    public R deleteMessageRefById(@Valid @RequestBody DeleteMessageRefByIdForm form) {
        long rows = messageService.deleteMessageRefById(form.getId());
        return R.ok().put("rows", rows);
    }
}
