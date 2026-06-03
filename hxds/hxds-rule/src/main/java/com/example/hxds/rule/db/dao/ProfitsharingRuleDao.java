package com.example.hxds.rule.db.dao;

import com.example.hxds.rule.db.pojo.ProfitsharingRuleEntity;
import java.util.HashMap;

public interface ProfitsharingRuleDao {
    public HashMap searchProfitsharingRuleById(long var1);

    public ProfitsharingRuleEntity searchCurrentRule(String var1);
}

