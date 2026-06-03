package com.example.hxds.rule.service.impl;


import com.example.hxds.rule.service.AwardRuleService;
import org.springframework.stereotype.Service;

@Service
public class AwardRuleImpl implements AwardRuleService {
    
    @Override
    public String calculateIncentiveFee(long driverId, String acceptTime) {
        return "66";
    }
}

