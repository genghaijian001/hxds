package com.example.hxds.snm.task;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.snm.entity.NewOrderMessage;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class NewOrderMassageTask {

    @Resource
    private ConnectionFactory factory;

    /**
     * 同步发送新订单消息
     */
    public void sendNewOrderMessage(ArrayList<NewOrderMessage> list){
        int ttl=1 * 60 * 1000; //新订单消息缓存过期时间1分钟
        String exchangeName = "new_order_private"; //交换机名字
        try (
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
        ){
            //定义交换机，根据routing key路由消息
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
            HashMap param=new HashMap();
            for (NewOrderMessage message : list){
                //MQ消息的属性信息
                HashMap map=new HashMap(){{
                    put("orderId",message.getOrderId());
                    put("from",message.getFrom());
                    put("to",message.getTo());
                    put("expectsFee",message.getExpectsFee());
                    put("mileage",message.getMileage());
                    put("minute",message.getMinute());
                    put("distance",message.getDistance());
                    put("favourFee",message.getFavourFee());
                }};
                // Fix-7: 添加 deliveryMode=2 持久化消息，防止服务重启丢失
                AMQP.BasicProperties properties=new AMQP.BasicProperties()
                        .builder()
                        .contentEncoding("UTF-8")
                        .headers(map)
                        .deliveryMode(2)  // 2=持久化消息，1=非持久化
                        .expiration(ttl+"")
                        .build();

                String queueName="queue_" +message.getUserId();  //队列名字
                String routingKey=message.getUserId();  // routing key
                //声明队列(持久化缓存消息，消息接收不加锁，消息全部接收完并不删除队列)
                channel.queueDeclare(queueName,true,false,false,param);
                channel.queueBind(queueName,exchangeName,routingKey);
                // Fix-7: 启用发送确认模式，等待 Broker 确认，防止消息静默丢失
                channel.confirmSelect();
                //向交换机发送消息，并附带routing key
                channel.basicPublish(exchangeName,routingKey,properties,("新订单" +message.getOrderId()).getBytes());
                // 等待 Broker 确认（最多5秒），超时则抛异常触发重试
                if (!channel.waitForConfirms(5000)) {
                    throw new HxdsException("RabbitMQ 消息发送未被确认，userId=" + message.getUserId());
                }
                log.debug(message.getUserId()+ "的新订单消息发送成功（已持久化并确认）");
            }
        }catch(Exception e){
            log.error("执行异常",e);
            throw new HxdsException("新订单消息发送失败");
        }
    }

    /**
     * 异步发送新订单消息
     */
    @Async
    public  void sendNewOrderMessageAsync(ArrayList<NewOrderMessage> list){
        sendNewOrderMessage(list);
    }


    /**
     * 同步接收新订单消息
     */
    public List<NewOrderMessage> receiveNewOrderMessage(long userId){
        String exchangeName = "new_order_private"; //交换机名字
        String queueName="queue_" + userId;  //队列名字
        String routingKey=userId+ "";  // routing key

        List<NewOrderMessage> list=new ArrayList();
        try(
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
        ){
            //定义交换机，routing key模式
            channel.exchangeDeclare(exchangeName,BuiltinExchangeType.DIRECT);
            //声明队列(持久化缓存消息，消息接收不加锁，消息全部接收完并不删除队列)
            channel.queueDeclare(queueName,true,false,false,null);
            //绑定要接收的队列
            channel.queueBind(queueName,exchangeName,routingKey);
            //为了避免一次性接收太多消息，我们采用限流的方式，每次接收10条消息，然后循环接收
            channel.basicQos(0,10,true);

            while (true) {
                //从队列中接收消息
                GetResponse response=channel.basicGet(queueName,false);
                if (response != null){
                    //消息属性对象
                    AMQP.BasicProperties properties=response.getProps();
                    Map<String,Object> map=properties.getHeaders();
                    String orderId = MapUtil.getStr(map, "orderId");
                    String from = MapUtil.getStr(map, "from");
                    String to = MapUtil.getStr(map, "to");
                    String expectsFee = MapUtil.getStr(map, "expectsFee");
                    String mileage = MapUtil.getStr(map, "mileage");
                    String minute = MapUtil.getStr(map, "minute");
                    String distance = MapUtil.getStr(map, "distance");
                    String favourFee = MapUtil.getStr(map, "favourFee");

                    //把新订单消息封装到对象中
                    NewOrderMessage message = new NewOrderMessage();
                    message.setOrderId(orderId);
                    message.setFrom(from);
                    message.setTo(to);
                    message.setExpectsFee(expectsFee);
                    message.setMileage(mileage);
                    message.setMinute(minute);
                    message.setDistance(distance);
                    message.setFavourFee(favourFee);

                    list.add(message);

                    byte [] body= response.getBody();
                    String msg=new String(body);
                    log.debug("从RabbitMQ接收的订单消息：" + msg);

                    //确认收到消息，让MQ删除该消息
                    long deliveryTag=response.getEnvelope().getDeliveryTag();
                    channel.basicAck(deliveryTag,false);
                }else {
                    break;
                }
            }
            ListUtil.reverse(list);//消息倒叙，新消息排在前面
            return list;
        }catch(Exception e){
            log.error("执行异常", e);
            throw new HxdsException("接收新订单失败");
        }
    }

    /**
     * 同步删除新订单消息队列
     */
    public void deleteNewOrderQueue(long userId){
        String exchangeName = "new_order_private"; //交换机名字
        String queueName="queue_" + userId;  //队列名字

        try(
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
        ){
            //定义交换机
            channel.exchangeDeclare(exchangeName,BuiltinExchangeType.DIRECT);
            //删除队列
            channel.queueDelete(queueName);
            log.debug(userId + "的新订单消息队列成功删除");
        }catch(Exception e){
            log.error(userId + "的新订单队列删除失败", e);
            throw new HxdsException("新订单队列删除失败");
        }
    }

    /**
     * 异步删除新订单消息队列
     */
    @Async
    public void deleteNewOrderQueueAsync(long userId){
        deleteNewOrderQueue(userId);
    }

    /**
     * 同步清空新订单消息队列
     */
    public void clearNewOrderQueue(long userId){
        String exchangeName = "new_order_private"; //交换机名字
        String queueName="queue_" + userId;  //队列名字

        try(
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()
        ){
            //定义交换机
            channel.exchangeDeclare(exchangeName,BuiltinExchangeType.DIRECT);
            // 先声明队列（参数与 sendNewOrderMessage 保持一致：durable=true, autoDelete=false）
            // 队列不存在时创建，已存在时幂等返回，避免 queuePurge 对不存在队列抛 NOT_FOUND 异常
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, String.valueOf(userId));
            //清空队列
            channel.queuePurge(queueName);
            log.debug(userId + "的新订单消息队列清空删除");
        }catch (Exception e){
            log.error(userId + "的新订单队列清空失败", e);
            throw new HxdsException("新订单队列清空失败");
        }
    }

    /**
     * 异步清空新订单消息队列
     * @param userId
     */
    @Async
    public void clearNewOrderQueueAsync(long userId){
        this.clearNewOrderQueue(userId);
    }
}
