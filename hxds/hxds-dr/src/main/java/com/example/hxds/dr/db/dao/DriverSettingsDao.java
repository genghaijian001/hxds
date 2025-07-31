package com.example.hxds.dr.db.dao;

import com.example.hxds.dr.db.pojo.DriverSettingsEntity;

import java.util.Map;

/**
 * @Entity com.example.hxdsdr.db.pojo.DriverSettingsEntity
 */
public interface DriverSettingsDao {
    //司机抢单设置
    public int insertDriverSettings(DriverSettingsEntity entity);
    //查询司机设置
    public String searchDriverSettings(long driverId);

}




