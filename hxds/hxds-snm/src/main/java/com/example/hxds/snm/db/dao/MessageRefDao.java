package com.example.hxds.snm.db.dao;

import com.example.hxds.snm.db.pojo.MessageRefEntity;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class MessageRefDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 插入数据
     */
    public String insert(MessageRefEntity entity){
        entity = mongoTemplate.save(entity);
        return entity.get_id();
    }

    /**
     * 查询未读消息数量
     */
    public long searchUnreadCount(long userId, String identity){
        Query query = new Query();
        query.addCriteria(Criteria.where("readFlag").is(false).and("receiverId")
                .is(userId).and("receiverIdentity").is(identity));
        long count = mongoTemplate.count(query, MessageRefEntity.class);
        return count;
    }

    /**
     * 轮询消息  把接收到的消息修改为已读
     */
    public long searchLastCount(long userId, String identity){
        Query query = new Query();
        query.addCriteria(Criteria.where("lastFlag").is(true).and("receiverId")
                .is(userId).and("receiverIdentity").is(identity));
        Update update = new Update();
        update.set("lastFlag",false);
        UpdateResult result = mongoTemplate.updateMulti(query, update, "message_ref");
        long rows = result.getModifiedCount();
        return rows;
    }

    /**
     * 把未读消息修改为已读
     */
    public long updateUnreadMessage(String id){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.set("readFlag",true);
        UpdateResult result = mongoTemplate.updateFirst(query, update, "message_ref");
        long rows = result.getModifiedCount();
        return rows;
    }

    /**
     * 根据主键值删除一条消息
     */
    public long deleteMessageRefById(String id){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        DeleteResult result = mongoTemplate.remove(query, "message_ref");
        long rows = result.getDeletedCount();
        return rows;
    }

    /**
     * 删除所有信息
     */
    public long deleteUserMessageRef(long userId, String identity){
        Query query = new Query();
        query.addCriteria(Criteria.where("receiverId").is(userId)
                .and("receiverIdentity").is(identity));
        DeleteResult result = mongoTemplate.remove(query, "message_ref");
        long rows = result.getDeletedCount();
        return rows;
    }

    /**
     * 分页查询用户消息ref列表
     */
    public ArrayList<HashMap> searchByPage(long userId, String identity, long page, long length) {
        Query query = new Query();
        query.addCriteria(Criteria.where("receiverId").is(userId).and("receiverIdentity").is(identity));
        query.with(Sort.by(Sort.Direction.DESC, "_id"));
        query.skip((page - 1) * length).limit((int) length);
        List<MessageRefEntity> refs = mongoTemplate.find(query, MessageRefEntity.class);
        ArrayList<HashMap> list = new ArrayList<>();
        for (MessageRefEntity ref : refs) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("id", ref.get_id());
            item.put("messageId", ref.getMessageId());
            item.put("readFlag", ref.getReadFlag());
            list.add(item);
        }
        return list;
    }


}
