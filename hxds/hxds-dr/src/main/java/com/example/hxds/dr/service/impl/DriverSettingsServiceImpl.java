package com.example.hxds.dr.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.example.hxds.dr.db.dao.DriverSettingsDao;
import com.example.hxds.dr.service.DriverSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

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

}
