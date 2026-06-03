package com.example.hxds.dr.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.hxds.dr.db.dao.DriverSettingsDao;
import com.example.hxds.dr.service.DriverSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DriverSettingsServiceImpl implements DriverSettingsService {

    @Resource
    private DriverSettingsDao driverSettingsDao;

    /**
     * 查询司机设置
     * @param driverId
     * @return
     */
    @Override
    public HashMap searchDriverSettings(long driverId) {
        String settings = driverSettingsDao.searchDriverSettings(driverId);
        // 使用一个JSON库的 parseObj() 方法将 settings 字符串解析为一个JSON对象。
        // 然后，它使用 toBean() 方法将JSON对象转换为 HashMap 对象。
        HashMap map = JSONUtil.parseObj(settings).toBean(HashMap.class);
        boolean bool= MapUtil.getInt(map,"listenService") == 1 ? true : false;
        map.replace("listenService",bool);
        bool= MapUtil.getInt(map,"autoAccept") == 1 ? true : false;
        map.replace("autoAccept",bool);
        return map;
    }

    @Override
    public int updateDriverSettings(long driverId, Map param) {
        String current = driverSettingsDao.searchDriverSettings(driverId);
        JSONObject json = JSONUtil.parseObj(current);
        if (param.containsKey("rangeDistance")) json.set("rangeDistance", param.get("rangeDistance"));
        if (param.containsKey("orderDistance")) json.set("orderDistance", param.get("orderDistance"));
        if (param.containsKey("orientation")) json.set("orientation", param.get("orientation"));
        if (param.containsKey("listenService")) json.set("listenService", param.get("listenService"));
        if (param.containsKey("autoAccept")) json.set("autoAccept", param.get("autoAccept"));
        HashMap<String, Object> updateParam = new HashMap<>();
        updateParam.put("driverId", driverId);
        updateParam.put("settings", json.toString());
        return driverSettingsDao.updateDriverSettings(updateParam);
    }

}
