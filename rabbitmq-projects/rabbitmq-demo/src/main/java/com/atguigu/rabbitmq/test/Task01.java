package com.atguigu.rabbitmq.test;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author： tks
 * @date： 2022/8/26
 */
public class Task01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNext()) {
                String message = scanner.next();
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println("发送消息完成:" + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
