package com.example.hxds.rule.db.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CancelRuleEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String rule;
    private String code;
    private String name;
    private Date createTime;
    private Byte type;
    private Byte status;
    private Long id;
}

