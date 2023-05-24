package com.atguigu.rabbitmq.four;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * 发布即确认
 * 发布一个、确认一个
 */
public class ConfirmMessage {
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws InterruptedException, TimeoutException, IOException {
        publishMessageIndividually();
    }

    // 单个确认
    public static void publishMessageIndividually() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();
        // 队列的声明
        String queueName = UUID.randomUUID().toString();
        System.out.println(queueName);
        channel.queueDeclare(queueName, true, false, false, null);

        channel.confirmSelect();

        // 开始时间
        long beginTime = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";

            channel.basicPublish("", queueName, null, message.getBytes());

            // 单个消息马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时" + (endTime - beginTime));
    }



    public static void publishMessageIndividually2() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        System.out.println(queueName);
        channel.queueDeclare(queueName, true, false, false, null);

        // 开启发布确认机制
        channel.confirmSelect();

        long beginTime = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());

            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发布成功");
            }
        }


    }

}
