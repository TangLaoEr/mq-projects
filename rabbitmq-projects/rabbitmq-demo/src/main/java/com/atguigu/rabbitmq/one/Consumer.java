package com.atguigu.rabbitmq.one;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author： tks
 * @date： 2022/7/20
 * @version： V1.0
 */
public class Consumer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        // channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        DeliverCallback callback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                System.out.println("消费内容：" + new String(delivery.getBody()));
            }
        };

        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {
                System.out.println("取消操作");
                System.out.println(s);
            }
        };

        channel.basicConsume(QUEUE_NAME, true, callback, cancelCallback);
        System.out.println("消费完毕");
    }

}
