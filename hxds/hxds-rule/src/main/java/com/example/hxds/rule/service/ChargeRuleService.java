/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.hxds.rule.db.pojo.ChargeRuleEntity
 *  com.example.hxds.rule.service.ChargeRuleService
 */
package com.example.hxds.rule.service;

import com.example.hxds.rule.db.pojo.ChargeRuleEntity;
import java.util.HashMap;

public interface ChargeRuleService {
    public HashMap searchChargeRuleById(long var1);

    public HashMap calculateOrderCharge(String var1, String var2, int var3, String var4);

    public int insert(ChargeRuleEntity var1);
}

