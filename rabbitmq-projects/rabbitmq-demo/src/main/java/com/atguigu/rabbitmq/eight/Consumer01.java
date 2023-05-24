package com.atguigu.rabbitmq.eight;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author： tks
 * @date： 2022/7/21
 * @version： V1.0
 */
public class Consumer01 {
    // 普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    // 死信交换机的名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    // 普通队列
    public static final String NORMAL_QUEUE = "normal_queue";
    // 死信队列
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 声明普通队列(需要特殊设置才能转发到死信队列)
        HashMap<String, Object> arguments = new HashMap<>();
        // 正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 设置死信Routingkey
        arguments.put("x-dead-letter-routing-key", "lisi");
        // 设置队列的最大长度
        // arguments.put("x-max-length", 6);

        // 设置过期时间
        // arguments.put("x-message-ttl", 10 * 1000); 可以在发送消息的时候再设置
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);

        // 声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        // 绑定普通的交换机与队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        // 绑定死心的交换机与队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        System.out.println("等待接收消息....");

        channel.basicConsume(NORMAL_QUEUE, false, (tag, message) -> {
            String msg = new String(message.getBody());
            if ("info5".equals(msg)) {
                System.out.println("拒绝消息info5");
                // 拒绝
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false); // false 表示不放回原队列
            }else {
                System.out.println("接收到消息:" + new String(message.getBody()));
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        }, message -> {
            System.out.println("接收消息失败");
        });
    }
}
