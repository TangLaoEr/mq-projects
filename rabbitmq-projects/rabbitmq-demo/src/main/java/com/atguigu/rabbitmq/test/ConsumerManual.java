package com.atguigu.rabbitmq.test;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 手动应答
 * @author： tks
 * @date： 2022/8/26
 */
public class ConsumerManual {
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) {
       new Thread(new MyWork(), "短线成").start();
    }

    static class MyWork implements Runnable {
        @Override
        public void run() {
            try {
                Channel channel = RabbitMqUtils.getChannel();
                DeliverCallback deliverCallback = (consumerTag, message) -> {
                    System.out.println(message);

                    System.out.println(new String(message.getBody()));

                    // 手动应答
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                };

                // 设置不公平分发
                channel.basicQos(1);

                channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, System.out::println);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }


        }
    }
}

