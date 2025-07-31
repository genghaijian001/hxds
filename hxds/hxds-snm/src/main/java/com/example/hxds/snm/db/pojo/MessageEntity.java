package com.example.hxds.snm.db.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * 集合有什么字段，取决于保存在其中的数据
 */
@Data
@Document(collection = "message")
public class MessageEntity implements Serializable {
    @Id
    private String _id;//自动生成的主键值

    @Indexed(unique = true)
    private String uuid;
    @Indexed
    private Long senderId;//发送者ID，就是用户ID。如果是系统自动发出，这个ID值是0

    private String senderIdentity;//发送者exchange  交换机

    private String senderPhoto = "http://static-1258386385.cos.ap-beijing.myqcloud.com/img/System.jpg";

    private String senderName;//发送者名称，也就是用户姓名。在消息页面要显示发送人的名字

    private String msg;//消息正文
    @Indexed
    private Date sendTime;//发送时间
}
