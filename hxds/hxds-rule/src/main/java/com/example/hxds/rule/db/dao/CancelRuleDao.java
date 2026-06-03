package com.example.hxds.rule.db.dao;

import com.example.hxds.rule.db.pojo.CancelRuleEntity;
import java.util.HashMap;
import java.util.Map;

public interface CancelRuleDao {
    public HashMap searchCancelRuleById(long var1);

    public CancelRuleEntity searchCurrentRule(Map var1);
}

