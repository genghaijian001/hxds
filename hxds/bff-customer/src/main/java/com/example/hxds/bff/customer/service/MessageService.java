package com.example.hxds.bff.customer.service;

import com.example.hxds.bff.customer.controller.form.ReceiveBillMessageForm;
import com.example.hxds.common.util.R;

public interface MessageService {
    public String receiveBillMessage(ReceiveBillMessageForm form);
}
