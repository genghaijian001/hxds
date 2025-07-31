package com.example.hxds.mps.service;

import java.util.HashMap;

public interface MapService {

    //封装预估里程和时间
    public HashMap estimateOrderMileageAndMinute(String mode,
                                                 String startPlaceLatitude,
                                                 String startPlaceLongitude,
                                                 String endPlaceLatitude,
                                                 String endPlaceLongitude);

    //mis端封装地图  计算行驶路线
    public HashMap calculateDriveLine(String startPlaceLatitude,
                                      String startPlaceLongitude,
                                      String endPlaceLatitude,
                                      String endPlaceLongitude);


}
