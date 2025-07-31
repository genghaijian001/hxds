package com.example.hxds.mps.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface DriverLocationService {

    //缓存司机实时定位  创建司机上线缓存
    public void updateLocationCache(Map param);

    //删除司机上线缓存
    public void removeLocationCache(long driverId);

    //GEO查找附近适合接单的司机
    public ArrayList searchBefittingDriverAboutOrder(double startPlaceLatitude,
                                                     double startPlaceLongitude,
                                                     double endPlaceLatitude,
                                                     double endPlaceLongitude,
                                                     double mileage);

    //司机上传定位 和 乘客端查询司机定位
    public void updateOrderLocationCache(Map param);
    public HashMap searchOrderLocationCache(long orderId);
}
