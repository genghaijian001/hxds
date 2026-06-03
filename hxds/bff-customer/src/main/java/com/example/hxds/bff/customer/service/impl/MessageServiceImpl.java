package com.example.hxds.bff.customer.service.impl;

import cn.hutool.core.map.MapUtil;
import com.example.hxds.bff.customer.controller.form.DeleteMessageRefByIdForm;
import com.example.hxds.bff.customer.controller.form.ReceiveBillMessageForm;
import com.example.hxds.bff.customer.controller.form.SearchMessageByIdForm;
import com.example.hxds.bff.customer.controller.form.SearchMessageByPageForm;
import com.example.hxds.bff.customer.feign.SnmServiceApi;
import com.example.hxds.bff.customer.service.MessageService;
import com.example.hxds.common.util.R;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private SnmServiceApi snmServiceApi;

    @Override
    public String receiveBillMessage(ReceiveBillMessageForm form) {
        R r = snmServiceApi.receiveBillMessage(form);
        return MapUtil.getStr(r, "result");
    }

    @Override
    public ArrayList searchMessageByPage(SearchMessageByPageForm form) {
        R r = snmServiceApi.searchMessageByPage(form);
        return (ArrayList) r.get("result");
    }

    @Override
    public HashMap searchMessageById(String id) {
        SearchMessageByIdForm form = new SearchMessageByIdForm();
        form.setId(id);
        R r = snmServiceApi.searchMessageById(form);
        return (HashMap) r.get("result");
    }

    @Override
    public long updateUnreadMessage(String id) {
        DeleteMessageRefByIdForm form = new DeleteMessageRefByIdForm();
        form.setId(id);
        R r = snmServiceApi.updateUnreadMessage(form);
        return MapUtil.getLong(r, "rows");
    }

    @Override
    public long deleteMessageRefById(String id) {
        DeleteMessageRefByIdForm form = new DeleteMessageRefByIdForm();
        form.setId(id);
        R r = snmServiceApi.deleteMessageRefById(form);
        return MapUtil.getLong(r, "rows");
    }
}
