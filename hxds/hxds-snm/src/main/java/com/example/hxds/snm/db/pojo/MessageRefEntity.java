package com.example.hxds.snm.db.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "message_ref")
public class MessageRefEntity implements Serializable {
    @Id
    private String _id;//主键
    @Indexed
    private String messageId;//message记录的_id  外键MessageEntity的_id
    @Indexed
    private Long receiverId;//接收人ID
    private String receiverIdentity;//接收人exchange  交换机
    @Indexed
    private Boolean readFlag;//是否已读
    @Indexed
    private Boolean lastFlag;//是否为新接收的消息
}
