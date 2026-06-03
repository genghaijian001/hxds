package com.example.hxds.rule.db.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProfitsharingRuleEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;
    private Long id;
    private Integer status;
    private String name;
    private Date createTime;
    private String rule;

}

