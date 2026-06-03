package com.example.hxds.snm.service.impl;

import com.example.hxds.snm.service.MessageService;
import com.example.hxds.snm.db.dao.MessageDao;
import com.example.hxds.snm.db.dao.MessageRefDao;
import com.example.hxds.snm.db.pojo.MessageEntity;
import com.example.hxds.snm.db.pojo.MessageRefEntity;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;


@Service
public class MessageServiceImpl implements MessageService {
    @Resource
    private MessageDao messageDao;

    @Resource
    private MessageRefDao messageRefDao;

    @Override
    public String insertMessage(MessageEntity entity) {
        String id = messageDao.insert(entity);
        return id;
    }

    @Override
    public HashMap searchMessageById(String id) {
        HashMap map = messageDao.searchMessageById(id);
        return map;
    }

    @Override
    public String insertRef(MessageRefEntity entity) {
        String id = messageRefDao.insert(entity);
        return id;
    }

    @Override
    public long searchUnreadCount(long userId, String identity) {
        long count = messageRefDao.searchUnreadCount(userId, identity);
        return count;
    }

    @Override
    public long searchLastCount(long userId, String identity) {
        long count = messageRefDao.searchLastCount(userId, identity);
        return count;
    }

    @Override
    public long updateUnreadMessage(String id) {
        long rows = messageRefDao.updateUnreadMessage(id);
        return rows;
    }

    @Override
    public long deleteMessageRefById(String id) {
        long rows = messageRefDao.deleteMessageRefById(id);
        return rows;
    }

    @Override
    public long deleteUserMessageRef(long userId, String identity) {
        long rows = messageRefDao.deleteUserMessageRef(userId, identity);
        return rows;
    }

    @Override
    public ArrayList<HashMap> searchMessageByPage(long userId, String identity, long page, long length) {
        ArrayList<HashMap> refs = messageRefDao.searchByPage(userId, identity, page, length);
        for (HashMap ref : refs) {
            String messageId = (String) ref.get("messageId");
            HashMap msg = messageDao.searchMessageById(messageId);
            if (msg != null) {
                ref.put("senderName", msg.get("senderName"));
                ref.put("senderPhoto", msg.get("senderPhoto"));
                ref.put("msg", msg.get("msg"));
                ref.put("sendTime", msg.get("sendTime"));
            }
        }
        return refs;
    }
}

