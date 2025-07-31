package com.example.hxds.snm.task;

import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.snm.db.pojo.MessageEntity;
import com.example.hxds.snm.db.pojo.MessageRefEntity;
import com.example.hxds.snm.service.MessageService;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MessageTask {
    @Resource
    private ConnectionFactory factory;
    @Resource
    private MessageService messageService;

    /**
     * 同步发送私有消息
     */
    public void sendPrivateMessage(String identity, long userId, Integer ttl, MessageEntity entity){
        /**
         * 发送消息之前保存到MongoDB\MessageEntity里边
         */
        String id = messageService.insertMessage(entity);
        /**
         * 调用MQ发送消息
         */
        String exchangeName = identity+"_private"; //交换机名字
        String queueName="queue_" +userId;  //队列名字
        String routingKey=userId+"";  // routing key
        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
        ){
            //定义交换机，根据routing key路由消息
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);

            HashMap param = new HashMap();

            //声明队列(持久化缓存消息，消息接收不加锁，消息全部接收完并不删除队列)
            channel.queueDeclare(queueName,true,false,false,param);
            HashMap map = new HashMap();
            map.put("messageId",id);

            //创建消息属性意向
            AMQP.BasicProperties properties;
            if (ttl != null && ttl >0){
                properties=new AMQP.BasicProperties().builder()
                        .contentEncoding("UTF-8")
                        .headers(map)
                        .expiration(ttl+"").build();
            }else {
                properties=new AMQP.BasicProperties().builder()
                        .contentEncoding("UTF-8")
                        .headers(map).build();
            }
            channel.basicPublish(exchangeName,routingKey,properties,entity.getMsg().getBytes());

            log.debug("消息发送成功");
        } catch (Exception e) {
            log.error("执行异常", e);
            throw new HxdsException("向MQ发送消息失败");
        }

    }

    /**
     * 异步发送私有消息
     */
    @Async
    public void sendPrivateMessageAsync(String identity, long userId, Integer ttl, MessageEntity entity){
        sendPrivateMessage(identity, userId, ttl, entity);
    }

    /**
     * 向一个交换机里面发送广播消息
     */
    public void sendBroadcastMessage(String identity, Integer ttl, MessageEntity entity) {
        /**
         * 发送消息之前保存到MongoDB\MessageEntity里边
         */
        String id = messageService.insertMessage(entity);
        /**
         * 调用MQ发送消息
         */
        String exchangeName = identity+"_broadcast"; //交换机名字

        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
        ){
            HashMap map = new HashMap();
            map.put("messageId",id);

            //创建消息属性意向
            AMQP.BasicProperties.Builder builder=new AMQP.BasicProperties().builder();
            //消息持久化存储
            builder.deliveryMode(MessageDeliveryMode.toInt(MessageDeliveryMode.PERSISTENT));
            builder.expiration(ttl.toString());//设置消息有效期

            //创建消息属性意向
            AMQP.BasicProperties properties=builder.headers(map).build();
            //定义交换机，根据routing key路由消息
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);
            channel.basicPublish(exchangeName,"",properties,entity.getMsg().getBytes());

            log.debug("消息发送成功");
        } catch (Exception e) {
            log.error("执行异常", e);
            throw new HxdsException("向MQ发送消息失败");
        }

    }

    /**
     * 异步 向一个交换机里面发送广播消息
     */
    @Async
    public void sendBroadcastMessageAsync(String identity, Integer ttl, MessageEntity entity) {
        this.sendBroadcastMessage(identity, ttl, entity);
    }


    /**
     * 同步接收消息
     */
    public int receive(String identity, long userId) {
        String privateExchangeName = identity + "_private";//私有交换机名字
        String broadcastExchangeName = identity + "_broadcast";//广播交换机名字
        String queueName = "queue_" + userId;//队列名字
        String routingKey = userId + "";// routing key

        int i=0;
        try(
                Connection connection = factory.newConnection();
                Channel privateChannel = connection.createChannel();
                Channel  broadcastChannel= connection.createChannel();
        ){
            //接收私有消息
            //定义交换机，routing key模式
            privateChannel.exchangeDeclare(privateExchangeName,BuiltinExchangeType.DIRECT);
            //声明队列(持久化缓存消息，消息接收不加锁，消息全部接收完并不删除队列)
            privateChannel.queueDeclare(queueName,true,false,false,null);
            //绑定要接收的队列
            privateChannel.queueBind(queueName,privateExchangeName,routingKey);
            while (true){
                //从队列中接收消息
                GetResponse response=privateChannel.basicGet(queueName,false);
                if (response != null) {
                    //消息属性对象
                    AMQP.BasicProperties properties=response.getProps();
                    Map<String,Object> map=properties.getHeaders();
                    String messageId = map.get("messageId").toString();
                    byte[] body = response.getBody();
                    String message = new String(body);
                    log.debug("从RabbitMQ接收的私有消息：" + message);

                    MessageRefEntity entity = new MessageRefEntity();
                    entity.setMessageId(messageId);
                    entity.setReceiverId(userId);
                    entity.setReceiverIdentity(identity);
                    entity.setReadFlag(false);
                    entity.setLastFlag(true);
                    messageService.insertRef(entity);
                    long deliveryTag = response.getEnvelope().getDeliveryTag();
                    privateChannel.basicAck(deliveryTag,false);
                    i++;
                }else{
                    break;
                }
            }

            //接收公有消息
            //定义交换机，routing key模式
            broadcastChannel.exchangeDeclare(broadcastExchangeName,BuiltinExchangeType.FANOUT);
            //声明队列(持久化缓存消息，消息接收不加锁，消息全部接收完并不删除队列)
            broadcastChannel.queueDeclare(queueName,true,false,false,null);
            //绑定要接收的队列
            broadcastChannel.queueBind(queueName,broadcastExchangeName,"");
            while (true){
                //从队列中接收消息
                GetResponse response=privateChannel.basicGet(queueName,false);
                if (response != null) {
                    //消息属性对象
                    AMQP.BasicProperties properties=response.getProps();
                    Map<String,Object> map=properties.getHeaders();
                    String messageId = map.get("messageId").toString();
                    byte[] body = response.getBody();
                    String message = new String(body);
                    log.debug("从RabbitMQ接收的私有消息：" + message);

                    MessageRefEntity entity = new MessageRefEntity();
                    entity.setMessageId(messageId);
                    entity.setReceiverId(userId);
                    entity.setReceiverIdentity(identity);
                    entity.setReadFlag(false);
                    entity.setLastFlag(true);
                    messageService.insertRef(entity);
                    long deliveryTag = response.getEnvelope().getDeliveryTag();
                    privateChannel.basicAck(deliveryTag,false);
                    i++;
                }else{
                    break;
                }
            }
        }catch (Exception e) {
            log.error("执行异常", e);
            throw new HxdsException("接收消息失败");
        }
        return i;
    }

    /**
     * 异步接收消息
     */
    @Async
    public void receiveAsync(String identity, long userId) {
        receiveAsync(identity, userId);
    }


    /**
     * 同步删除消息队列
     */
    public void deleteQueue(String identity, long userId) {
        String privateExchangeName = identity + "_private";//私有交换机名字
        String broadcastExchangeName = identity + "_broadcast";//广播交换机名字
        String queueName = "queue_" + userId;//队列名字

        try(
                Connection connection = factory.newConnection();
                Channel privateChannel = connection.createChannel();
                Channel  broadcastChannel= connection.createChannel();
        ){
            //定义交换机，routing key模式
            privateChannel.exchangeDeclare(privateExchangeName,BuiltinExchangeType.DIRECT);
            privateChannel.queueDelete(queueName);
            //定义交换机，routing key模式
            broadcastChannel.exchangeDeclare(broadcastExchangeName,BuiltinExchangeType.FANOUT);
            broadcastChannel.queueDelete(queueName);

            log.debug("消息队列成功删除");
        } catch (Exception e) {
            log.error("删除队列失败", e);
            throw new HxdsException("删除队列失败");
        }
    }

    /**
     * 异步删除消息队列
     */
    @Async
    public void deleteQueueAsync(String identity, long userId) {
        deleteQueue(identity, userId);
    }


    /**
     * 同步接收私有消息
     */
    public String receiveBillMessage(String identity, long userId){
        String exchangeName = identity+"_private"; //交换机名字
        String queueName="queue_" +userId;  //队列名字
        String routingKey=userId+"";  // routing key
        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
        ) {
            //定义交换机，根据routing key路由消息
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
            //声明队列(持久化缓存消息，消息接收不加锁，消息全部接收完并不删除队列)
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName,exchangeName,routingKey);
            channel.basicQos(0,1,true);

            //发送接收
            GetResponse response = channel.basicGet(queueName, false);
            if (response != null) {
                AMQP.BasicProperties properties = response.getProps();
                Map<String, Object> map = properties.getHeaders();
                String messageId = map.get("messageId").toString();
                byte[] body = response.getBody();
                String msg = new String(body);

                log.debug("从RabbitMQ接收的订单消息：" + msg);

                //把接收到的消息保存到MessageRef集合
                MessageRefEntity entity = new MessageRefEntity();
                entity.setMessageId(messageId);
                entity.setReceiverId(userId);
                entity.setReceiverIdentity(identity);
                entity.setReadFlag(true);
                entity.setLastFlag(true);
                messageService.insertRef(entity);

                long deliveryTag = response.getEnvelope().getDeliveryTag();
                channel.basicAck(deliveryTag,false);

                return msg;
            }
            return "";
        }catch (Exception e) {
            log.error("执行异常", e);
            throw new HxdsException("接收新订单失败");
        }
    }


}
