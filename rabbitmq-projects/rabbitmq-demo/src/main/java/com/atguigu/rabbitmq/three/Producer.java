package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 消息在手动应答时是不丢失的、放回队列中重新消费
 * 两个持久化
 * 1.队列持久化
 * 2.消息持久化
 */
public class Producer {
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        // 需要让Queue进行持久化
        boolean durable = true;
        // 声明队列
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);

        // 从控制台当中接受消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            // channel.basicPublish("", TASK_QUEUE_NAME, null, message.getBytes("UTF-8"));

            // 消息持久化
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息:" + message);
        }
    }
}
