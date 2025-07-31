package com.example.hxds.odr.db.dao;


import com.example.hxds.odr.db.pojo.OrderCommentEntity;

import java.util.HashMap;
import java.util.Map;

public interface OrderCommentDao {
    //添加订单评价
    public int insert(OrderCommentEntity entity);

    //司机查询订单评价详情
    public HashMap searchCommentByOrderId(Map param);

}




