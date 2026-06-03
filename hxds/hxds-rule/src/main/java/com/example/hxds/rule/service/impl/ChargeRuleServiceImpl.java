package com.example.hxds.rule.service.impl;

import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.rule.bean.Voucher;
import com.example.hxds.rule.db.dao.ChargeRuleDao;
import com.example.hxds.rule.db.pojo.ChargeRuleEntity;
import com.example.hxds.rule.service.ChargeRuleService;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.IExpressContext;
import java.util.HashMap;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChargeRuleServiceImpl  implements ChargeRuleService {


    @Override
    public HashMap searchChargeRuleById(long var1) {
        return null;
    }

    @Override
    public HashMap calculateOrderCharge(String var1, String var2, int var3, String var4) {
        return null;
    }

    @Override
    public int insert(ChargeRuleEntity var1) {
        return 0;
    }
}

