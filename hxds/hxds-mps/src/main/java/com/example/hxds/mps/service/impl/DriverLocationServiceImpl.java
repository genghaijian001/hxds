package com.example.hxds.mps.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import com.example.hxds.mps.service.DriverLocationService;
import com.example.hxds.mps.util.CoordinateTransform;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class DriverLocationServiceImpl implements DriverLocationService {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 缓存司机实时定位  创建司机上线缓存
     * @param param
     */
    @Override
    public void updateLocationCache(Map param) {
        long driverId = MapUtil.getLong(param, "driverId");
        String latitude = MapUtil.getStr(param, "latitude");
        String longitude = MapUtil.getStr(param, "longitude");

        //接单范围
        int rangeDistance = MapUtil.getInt(param, "rangeDistance");
        //订单里程范围
        int orderDistance = MapUtil.getInt(param, "orderDistance");

        //封装成Point对象才能缓存到Redis里面
        Point point = new Point(Convert.toDouble(longitude), Convert.toDouble(latitude));

        /**
         * Fix-8: 把司机实时定位缓存到Redis GEO中，便于Geo定位计算。
         * GEO是集合形式，不能对单个元素设置过期时间。
         * 通过 driver_online# key 的过期（65秒）来标记司机是否在线，搜索时先过滤。
         * removeLocationCache 下线时同时删除 GEO 数据，防止幽灵司机。
         */
        redisTemplate.opsForGeo().add("driver_location", point, driverId + "");

        //定向接单地址的经度
        String orientateLongitude=null;
        if (param.get("orientateLongitude") != null ){
            orientateLongitude=MapUtil.getStr(param, "orientateLongitude");
        }
        //定向接单地址的纬度
        String orientateLatitude = null;
        if (param.get("orientateLatitude") !=null ){
            orientateLatitude=MapUtil.getStr(param, "orientateLatitude");
        }
        //定向接单经纬度的字符串
        String orientation="none";
        if (orientateLongitude !=null && orientateLatitude !=null){
            orientation=orientateLatitude+","+orientateLongitude;
        }
        /**
         * 为了解决判断哪些司机在线，我们还要单独弄一个上线缓存
         * Fix-8: 在线key过期时间设为65秒（心跳60秒+5秒容错），与GEO不同步时通过hasKey过滤
         * 司机定期上报位置时刷新此key，超过65秒未上报则被认为离线
         */
        String temp=rangeDistance + "#" + orderDistance + "#" + orientation;
        redisTemplate.opsForValue().set("driver_online#"+driverId, temp, 65, TimeUnit.SECONDS);
    }

    /**
     * 删除司机上线缓存
     * @param driverId
     */
    @Override
    public void removeLocationCache(long driverId) {
        //删除司机定位缓存
        redisTemplate.opsForGeo().remove("driver_location", driverId + "");
        //删除司机上线缓存
        redisTemplate.delete("driver_online#" + driverId);
    }

    /**
     * GEO查找附近适合接单的司机
     * @param startPlaceLatitude  纬度
     * @param startPlaceLongitude  经度
     * @param endPlaceLatitude  目的地纬度
     * @param endPlaceLongitude  目的地经度
     * @param mileage    里程
     * @return
     */
    @Override
    public ArrayList searchBefittingDriverAboutOrder(double startPlaceLatitude,
                                                     double startPlaceLongitude,
                                                     double endPlaceLatitude,
                                                     double endPlaceLongitude,
                                                     double mileage) {
        //搜索订单起始点5公里以内的司机  对象使用起始地点的纬度和经度。
        Point point=new Point(startPlaceLongitude,startPlaceLatitude);
        //设置GEO距离单位为千米
        Metric metric= RedisGeoCommands.DistanceUnit.KILOMETERS;
        Distance distance= new Distance(5,metric);  //表示5千米的距离
        Circle circle= new Circle(point,distance);

        //创建GEO参数
        RedisGeoCommands.GeoRadiusCommandArgs args=RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .includeDistance()  //结果中包含距离
                .includeCoordinates() //结果中包含坐标
                .sortAscending(); //升序排列

        //执行GEO计算，获得查询结果
        GeoResults<RedisGeoCommands.GeoLocation<String>> radius =
                redisTemplate.opsForGeo().radius("driver_location", circle, args);

        ArrayList list=new ArrayList();  //需要通知的司机列表
        //如果查询结果不为空，则通过迭代器遍历查询结果。
        if (radius != null){
            Iterator<GeoResult<RedisGeoCommands.GeoLocation<String>>> iterator=radius.iterator();
            while (iterator.hasNext()){
                GeoResult<RedisGeoCommands.GeoLocation<String>> result = iterator.next();
                RedisGeoCommands.GeoLocation<String> content = result.getContent();
                //对于每一个结果，获取司机的ID和距离中心点的距离。
                String driverId = content.getName();
                double dist = result.getDistance().getValue();  // 距离中心点的距离

                //排查掉不在线的司机
                if (!redisTemplate.hasKey("driver_online#" + driverId)){
                    continue;
                }

                //查找该司机的在线缓存
                Object obj=redisTemplate.opsForValue().get("driver_online#" + driverId);
                //如果查找的那一-刻，缓存超时被置空，那么就忽略该司机
                if (obj == null){
                    continue;
                }
                //解析缓存中的数据，包括接单范围、订单里程和定向接单。
                String value = obj.toString();
                String[] temp = value.split("#");
                int rangeDistance = Integer.parseInt(temp[0]); //接单范围
                int orderDistance = Integer.parseInt(temp[1]); //订单里程
                String orientation = temp[2]; //定向接单

                //判断是否符合接单范围
                boolean bool_1 = (dist <= rangeDistance); // 司机定位点距离上车点的距离 <= 司机规定接单范围

                //判断订单里程是否符合
                boolean bool_2 = false;

                if (orderDistance == 0) {
                    bool_2 = true;
                } else if (orderDistance == 5 && mileage > 0 && mileage <= 5) {
                    bool_2 = true;
                } else if (orderDistance == 10 && mileage > 5 && mileage <= 10) {
                    bool_2 = true;
                } else if (orderDistance == 15 && mileage > 10 && mileage <= 15) {
                    bool_2 = true;
                } else if (orderDistance == 30 && mileage > 15 && mileage <= 30) {
                    bool_2 = true;
                }

                //判断定向接单是否符合
                boolean bool_3 = false;

                if (!orientation.equals("none")){
                    double orientationLatitude=Double.parseDouble(orientation.split(",")[0]);
                    double orientationLongitude=Double.parseDouble(orientation.split(",")[1]);
                    //把定向点的火星坐标转换成GPS坐标
                    double[] location= CoordinateTransform.transformGCJ02ToWGS84(orientationLongitude,orientationLatitude);
                    GlobalCoordinates point_1=new GlobalCoordinates(location[1],location[0]);
                    //把订单终点的火星坐标转换成GPS坐标
                    location=CoordinateTransform.transformGCJ02ToWGS84(endPlaceLongitude,endPlaceLatitude);
                    GlobalCoordinates point_2=new GlobalCoordinates(location[1],location[0]);
                    //这里不需要Redis的GEO计算，直接用封装函数计算两个GPS坐标之间的距离
                    GeodeticCurve geoCurve=new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.WGS84,point_1,point_2);
                    if (geoCurve.getEllipsoidalDistance() <= 3000){
                        bool_3=true;
                    }
                }else {
                    bool_3=true;
                }

                //判断司机是否符合接单条件。如果符合，则将司机的ID和距离添加到列表中。
                if (bool_1 && bool_2 && bool_3) {
                    HashMap map = new HashMap() {{
                        put("driverId", driverId);
                        put("distance", dist);
                    }};
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * 司机上传定位
     * @param param
     */
    @Override
    public void updateOrderLocationCache(Map param) {
        long orderId = MapUtil.getLong(param, "orderId");
        String latitude = MapUtil.getStr(param, "latitude");
        String longitude = MapUtil.getStr(param, "longitude");
        String location=latitude+"#"+longitude;
        //缓存在Redis  缓存10分钟
        redisTemplate.opsForValue().set("order_location#"+orderId,location,10,TimeUnit.MINUTES);
    }

    /**
     * 乘客端查询司机定位
     * @param orderId
     * @return
     */
    @Override
    public HashMap searchOrderLocationCache(long orderId) {
        Object obj = redisTemplate.opsForValue().get("order_location#" + orderId);
        if(obj!=null){
            String[] temp = obj.toString().split("#");
            String latitude = temp[0];
            String longitude = temp[1];
            HashMap map=new HashMap(){{
                put("latitude",latitude);
                put("longitude",longitude);
            }};
            return map;
        }
        return null;
    }
}
