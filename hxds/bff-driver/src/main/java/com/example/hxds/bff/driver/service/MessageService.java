package com.example.hxds.bff.driver.service;

import com.example.hxds.bff.driver.controller.form.SearchMessageByPageForm;

import java.util.ArrayList;
import java.util.HashMap;

public interface MessageService {
    ArrayList searchMessageByPage(SearchMessageByPageForm form);

    HashMap searchMessageById(String id);

    long deleteMessageRefById(String id);
}
