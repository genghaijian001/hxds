package com.example.hxds.rule.db.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ChargeRuleEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private Byte status;
    private Long id;
    private String name;
    private Date createTime;
    private String rule;

}

