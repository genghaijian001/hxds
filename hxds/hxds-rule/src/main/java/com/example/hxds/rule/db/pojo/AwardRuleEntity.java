package com.example.hxds.rule.db.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AwardRuleEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String code;
    private Long id;
    private Date createTime;
    private String rule;
    private Byte status;
    
}

