package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author： tks
 * @date： 2022/7/20
 * @version： V1.0
 * 生产者 发送大量消息
 */
public class Task01 {
    public static final String QUEUE_NAME = "hello";

    // 发送大量消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        /**
         * 声明队列
         * 1.队列名称
         * 2.消息是否持久化，默认情况消息存储在内存中
         * 3.是否消息共享，true可以多个消费者消费，false只能一个消费者消费
         * 4.是否自动删除，最后一个消费者断开连接以后，该队列是否自动删除，false不自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false,  false, false, null);


        // 从控制台当中接受消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成:" + message);
        }
    }
}
