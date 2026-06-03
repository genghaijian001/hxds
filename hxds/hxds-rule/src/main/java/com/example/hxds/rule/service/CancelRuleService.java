package com.example.hxds.rule.service;

import java.util.HashMap;

public interface CancelRuleService {
    public HashMap searchCancelRuleById(long var1);
    public HashMap calculateDriverCancelOrder(String var1, int var2, int var3, int var4, String var5);
}

