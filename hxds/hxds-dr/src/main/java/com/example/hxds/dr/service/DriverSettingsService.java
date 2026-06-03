package com.example.hxds.dr.service;

import java.util.HashMap;
import java.util.Map;

public interface DriverSettingsService {
    //查询司机设置
    public HashMap searchDriverSettings(long driverId);

    //更新司机设置
    public int updateDriverSettings(long driverId, Map param);
}
