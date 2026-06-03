package com.example.hxds.bff.driver.service.impl;

import com.example.hxds.bff.driver.controller.form.DeleteMessageRefByIdForm;
import com.example.hxds.bff.driver.controller.form.SearchMessageByIdForm;
import com.example.hxds.bff.driver.controller.form.SearchMessageByPageForm;
import com.example.hxds.bff.driver.feign.SnmServiceApi;
import com.example.hxds.bff.driver.service.MessageService;
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
    public long deleteMessageRefById(String id) {
        DeleteMessageRefByIdForm form = new DeleteMessageRefByIdForm();
        form.setId(id);
        R r = snmServiceApi.deleteMessageRefById(form);
        return Long.parseLong(r.get("rows").toString());
    }
}
