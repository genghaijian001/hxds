package com.example.hxds.dr.service;

import com.example.hxds.common.util.PageUtils;

import java.util.HashMap;
import java.util.Map;

public interface DriverService {

    //添加司机记录
    public String registerNewDriver(Map param);

    //司机实名认证
    public int updateDriverAuth(Map param);

    //开通活体检测 创建面部模型
    public String createDriverFaceModel(long driverId, String photo);

    //登录
    public HashMap login(String code);

    //查询司机个人汇总信息
    public HashMap searchDriverBaseInfo(long driverId);

    //查询司机分页记录
    public PageUtils searchDriverByPage(Map param);

    //查询司机实名认证信息
    public HashMap searchDriverAuth(long driverId);

    //查询司机实名认证申请
    public HashMap searchDriverRealSummary(long driverId);

    //更新司机备案状态
    public int updateDriverRealAuth(Map param);

    //mis查询司机信息  折叠面板
    public HashMap searchDriverBriefInfo(long driverId);

    public String searchDriverOpenId(long driverId);

}
