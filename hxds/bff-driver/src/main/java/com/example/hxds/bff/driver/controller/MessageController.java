package com.example.hxds.bff.driver.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.example.hxds.bff.driver.controller.form.DeleteMessageRefByIdForm;
import com.example.hxds.bff.driver.controller.form.SearchMessageByIdForm;
import com.example.hxds.bff.driver.controller.form.SearchMessageByPageForm;
import com.example.hxds.bff.driver.service.MessageService;
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
@Tag(name = "MessageController", description = "消息模块Web接口")
public class MessageController {

    @Resource
    private MessageService messageService;

    @PostMapping("/searchMessageByPage")
    @SaCheckLogin
    @Operation(summary = "分页查询司机消息")
    public R searchMessageByPage(@Valid @RequestBody SearchMessageByPageForm form) {
        long driverId = StpUtil.getLoginIdAsLong();
        form.setUserId(driverId);
        form.setIdentity("driver");
        ArrayList list = messageService.searchMessageByPage(form);
        return R.ok().put("result", list);
    }

    @PostMapping("/searchMessageById")
    @SaCheckLogin
    @Operation(summary = "根据ID查询消息")
    public R searchMessageById(@Valid @RequestBody SearchMessageByIdForm form) {
        HashMap map = messageService.searchMessageById(form.getId());
        return R.ok().put("result", map);
    }

    @PostMapping("/deleteMessageRefById")
    @SaCheckLogin
    @Operation(summary = "删除消息")
    public R deleteMessageRefById(@Valid @RequestBody DeleteMessageRefByIdForm form) {
        long rows = messageService.deleteMessageRefById(form.getId());
        return R.ok().put("rows", rows);
    }
}
