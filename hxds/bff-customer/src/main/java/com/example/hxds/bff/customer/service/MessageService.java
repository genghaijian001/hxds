package com.example.hxds.bff.customer.service;

import com.example.hxds.bff.customer.controller.form.ReceiveBillMessageForm;
import com.example.hxds.bff.customer.controller.form.SearchMessageByPageForm;

import java.util.ArrayList;
import java.util.HashMap;

public interface MessageService {
    String receiveBillMessage(ReceiveBillMessageForm form);

    ArrayList searchMessageByPage(SearchMessageByPageForm form);

    HashMap searchMessageById(String id);

    long updateUnreadMessage(String id);

    long deleteMessageRefById(String id);
}
