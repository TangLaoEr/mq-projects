package com.atguigu.rabbitmq.one;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author： tks
 * @date： 2022/7/20
 * @version： V1.0
 */
public class Producer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        HashMap<String, Object> arguments = new HashMap<>();
        // 官方允许是0-255之间，此处设置10，允许优化级范围为0-10，不要设置过大，浪费CPU与内存
        arguments.put("x-max-priority", 10);
        channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);
        String message = "hello world";

        for (int i = 1; i < 11; i++) {
            if (i == 5) {
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("", QUEUE_NAME, properties, ("info" + i).getBytes());
            }else {
                channel.basicPublish("", QUEUE_NAME, null, ("info" + i).getBytes());
            }
        }

        /**
         * 发送一个消费者信息
         * 1.发送到哪个交换机
         * 2.路由的key值是哪个，本次是队列的名称
         * 3.其他参数信息
         * 4.发送消息的消息体
         */
        System.out.println("发送完毕");
    }

}
