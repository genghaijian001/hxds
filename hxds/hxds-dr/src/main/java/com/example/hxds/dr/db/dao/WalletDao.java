package com.example.hxds.dr.db.dao;


import com.example.hxds.dr.db.pojo.DriverSettingsEntity;
import com.example.hxds.dr.db.pojo.WalletEntity;

import java.util.HashMap;
import java.util.Map;

public interface WalletDao {
    //司机钱包记录
    public int insert(WalletEntity entity);

    HashMap searchWalletByDriverId(long driverId);

}




