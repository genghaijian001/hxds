package com.example.hxds.dr.db.dao;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface DriverDao {
    //根据openId或者driverId查询是否存在司机记录
    public long hasDriver(Map param);
    //添加司机记录
    public int registerNewDriver(Map param);
    //根据openId查询插入记录的主键值。
    public String searchDriverId(String openId);
    //司机实名认证
    public int updateDriverAuth(Map param);
    //开通活体检测  查询司机姓名和性别
    public HashMap searchDriverNameAndSex(long driverId);
    //开通活体检测  更新司机表的Archive字段 面部档案
    public int updateDriverArchive(long driverId);
    //登录
    public HashMap login(String openId);
    //查询司机个人汇总信息
    public HashMap searchDriverBaseInfo(long driverId);
    //查询司机分页记录
    public ArrayList<HashMap> searchDriverByPage(Map param);
    //查询司机分页总数
    public long searchDriverCount(Map param);
    //查询司机实名认证信息
    public HashMap searchDriverAuth(long driverId);
    //司机微服务中查询司机实名认证申请
    public HashMap searchDriverRealSummary(long driverId);
    //司机微服务中更新司机备案状态
    public int updateDriverRealAuth(Map param);

    //mis查询司机信息  折叠面板
    public HashMap searchDriverBriefInfo(long driverId);

    public String searchDriverOpenId(long driverId);
}




