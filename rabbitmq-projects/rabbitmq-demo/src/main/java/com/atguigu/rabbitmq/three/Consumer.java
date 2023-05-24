package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.atguigu.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者手动应答
 */
public class Consumer {
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        new Thread(new MyWork(), "短线程").start();
    }

    static class MyWork implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + "正在等待。。。");
                Channel channel = RabbitMqUtils.getChannel();

                DeliverCallback deliverCallback = (consumerTag, message) -> {
                    SleepUtils.sleep(1);
                    System.out.println(Thread.currentThread().getName() + ">>1s接收到的消息:" + new String(message.getBody()));

                    /**
                     * 手动应答
                     * 1.消息的标记 tag
                     * 2.是否批量应答 false: 不批量应答信道中的消息 true:批量
                     */
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                };

                // 设置不公平分发
                int prefetchCount = 1;
                channel.basicQos(prefetchCount);

                boolean autoAck = false;
                channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, s -> System.out.println("消费者取消消费"));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
