package com.atguigu.rabbitmq.four;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author： tks
 * @date： 2022/7/20
 * @version： V1.0
 */
public class ConfirmMulMessage {
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

        // 开启发布确认
        channel.confirmSelect();

        // 批量确认消息大小
        int batchSize = 100;

        // 开始时间
        long beginTime = System.currentTimeMillis();

        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());

            // 判断达到100条消息的时候 批量确认一次
            if (i % batchSize == 0) {
                // 满一百次就确认一次
                channel.waitForConfirms();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个多个确认消息，耗时" + (endTime - beginTime));
    }
}
