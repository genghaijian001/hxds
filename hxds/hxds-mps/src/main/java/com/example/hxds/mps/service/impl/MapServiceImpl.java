package com.example.hxds.mps.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.mps.service.MapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

@Service
@Slf4j
public class MapServiceImpl implements MapService {

    //预估里程的API地址
    private String distanceUrl = "https://apis.map.qq.com/ws/distance/v1/matrix/";

    //规划行进路线的API地址
    private String directionUrl = "https://apis.map.qq.com/ws/direction/v1/driving/";

    @Value("${tencent.map.key}")
    private String key;

    /**
     * 预估里程和时间
     * @param mode  行驶模式
     * @param startPlaceLatitude  起点纬度
     * @param startPlaceLongitude  起点经度
     * @param endPlaceLatitude  终点纬度
     * @param endPlaceLongitude  终点经度
     * @return
     */
    @Override
    public HashMap estimateOrderMileageAndMinute(String mode,
                                                 String startPlaceLatitude,
                                                 String startPlaceLongitude,
                                                 String endPlaceLatitude,
                                                 String endPlaceLongitude) {

        HttpRequest req=new HttpRequest(distanceUrl);
        //使用form()方法将请求参数mode、from、to和key添加到请求中。
        // 最后，它使用execute()方法发送请求，并将响应结果转换为JSON格式的JSONObject对象。
        // 其中，status和message分别表示请求的状态码和响应消息。
        req.form("mode", mode);
        req.form("from", startPlaceLatitude + "," + startPlaceLongitude);
        req.form("to", endPlaceLatitude + "," + endPlaceLongitude);
        req.form("key",key);
        HttpResponse resp = req.execute();
        JSONObject json = JSONUtil.parseObj(resp.body());
        int status = json.getInt("status");
        String message=json.getStr("message");
        if(status!=0){
            log.error(message);
            throw new HxdsException("预估里程异常：" + message);
        }

        JSONArray rows = json.getJSONObject("result").getJSONArray("rows");
        JSONObject element = rows.get(0, JSONObject.class).getJSONArray("elements").get(0, JSONObject.class);
        int distance = element.getInt("distance");
        // distance(米)转公里，向上取整保留1位小数，最小0.1公里
        // 防止distance=0时产生"0"字符串，导致hxds-rule正则校验失败（mileage内容不正确）
        BigDecimal distanceBD = new BigDecimal(distance);
        BigDecimal mileageBD;
        if (distanceBD.compareTo(BigDecimal.ZERO) <= 0) {
            mileageBD = new BigDecimal("0.1");
        } else {
            mileageBD = distanceBD.divide(new BigDecimal(1000), 1, RoundingMode.CEILING);
            if (mileageBD.compareTo(BigDecimal.ZERO) <= 0) {
                mileageBD = new BigDecimal("0.1");
            }
        }
        String mileage = mileageBD.toPlainString();
        //从JSON对象中获取duration属性，并将其转换为整数。
        int duration=element.getInt("duration");
        //将duration除以60，得到分钟数。
        //将分钟数转换为字符串，并使用BigDecimal类进行四舍五入。
        //将分钟数转换为整数。
        String temp=new BigDecimal(duration).divide(new BigDecimal(60),0, RoundingMode.CEILING).toString();
        int minute=Integer.parseInt(temp);

        HashMap map=new HashMap(){{
            put("mileage",mileage);
            put("minute",minute);
        }};
        return map;
    }

    /**
     * mis端封装地图  计算行驶路线
     * @param startPlaceLatitude  起点纬度
     * @param startPlaceLongitude  起点经度
     * @param endPlaceLatitude  终点纬度
     * @param endPlaceLongitude  终点经度
     * @return
     */
    @Override
    public HashMap calculateDriveLine(String startPlaceLatitude,
                                      String startPlaceLongitude,
                                      String endPlaceLatitude,
                                      String endPlaceLongitude) {

        HttpRequest req=new HttpRequest(directionUrl);
        req.form("from",startPlaceLatitude+","+startPlaceLongitude);
        req.form("to",endPlaceLatitude+","+endPlaceLongitude);
        req.form("key",key);
        HttpResponse resp=req.execute();
        JSONObject json=JSONUtil.parseObj(resp.body());
        int status=json.getInt("status");
        String message = json.getStr("message");
        if (status !=0){
            log.error(message);
            throw new HxdsException("预估里程异常： "+ message);
        }
        JSONObject result=json.getJSONObject("result");
        HashMap map=result.toBean(HashMap.class);
        return map;
    }
}
