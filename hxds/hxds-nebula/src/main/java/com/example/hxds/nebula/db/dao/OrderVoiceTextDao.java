package com.example.hxds.nebula.db.dao;

import com.example.hxds.nebula.db.pojo.OrderVoiceTextEntity;

import java.util.Map;

public interface OrderVoiceTextDao {
    //上传文本录音文件
    public int insert(OrderVoiceTextEntity entity);
    //查询审查的内容
    public Long searchIdByUuid(String uuid);
    //审查结果
    public int updateCheckResult(Map param);
}
