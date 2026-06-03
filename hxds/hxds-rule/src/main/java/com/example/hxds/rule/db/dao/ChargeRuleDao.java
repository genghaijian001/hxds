package com.example.hxds.rule.db.dao;

import com.example.hxds.rule.db.pojo.ChargeRuleEntity;
import java.util.HashMap;

public interface ChargeRuleDao {
    public int insert(ChargeRuleEntity var1);

    public HashMap searchChargeRuleById(long var1);

    public ChargeRuleEntity searchCurrentRule(String var1);
}

