package com.example.hxds.rule.service.impl;

import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.rule.controller.form.SearchChargeRuleByIdForm;
import com.example.hxds.rule.db.dao.OrderCommentDao;
import com.example.hxds.rule.db.dao.OrderDao;
import com.example.hxds.rule.db.dao.ProfitsharingRuleDao;
import com.example.hxds.rule.service.ProfitsharingRuleService;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import java.util.HashMap;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProfitsharingRuleServiceImpl implements ProfitsharingRuleService {
    @Override
    public HashMap calculateProfitsharing(long var1, String var3, String var4) {
        return null;
    }

    @Override
    public HashMap searchProfitsharingRuleById(long var1) {
        return null;
    }
}

