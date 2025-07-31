package com.example.hxds.snm.db.dao;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.hxds.snm.db.pojo.MessageEntity;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;

@Repository
public class MessageDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 插入数据
     */
    public String insert(MessageEntity entity){
        Date sendTime = entity.getSendTime();
        /**
         * 因为MongoDB的时区是格林尼治(比北京时区晚8小时)
         * 保存的日期会自动被MongoDB减去8小时， 所以我们提前给日期增加8小时
         */
        sendTime = DateUtil.offset(sendTime, DateField.HOUR, 8);
        entity.setSendTime(sendTime);
        entity = mongoTemplate.save(entity);
        return entity.get_id();
    }

    /**
     * 根据id查询消息
     */
    public HashMap searchMessageById(String id){
        HashMap map = mongoTemplate.findById(id, HashMap.class, "message");
        Date sendTime = (Date) map.get("sendTime");
        sendTime = DateUtil.offset(sendTime, DateField.HOUR, -8);
        map.replace("sendTime",DateUtil.format(sendTime,"yyy-MM-dd HH-mm"));
        return map;
    }

    /**
     * 删除消息 接收者ID、交换机名称
     */
    public long deleteUserMessage(int receiverId, String identity){
        Query query = new Query();
        query.addCriteria(Criteria.where("receiverId").is(receiverId).and("receiverIdentity").is(identity));
        DeleteResult result = mongoTemplate.remove(query, "message");
        long rows = result.getDeletedCount();
        return rows;
    }
}
