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
public class Consumer02 {
    // 死信队列
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        channel.basicConsume(DEAD_QUEUE, true, (tag, message) -> {
            System.out.println("接收到死信消息》》》:" + new String(message.getBody()));
        }, message -> {
            System.out.println("接收消息失败");
        });
    }
}
