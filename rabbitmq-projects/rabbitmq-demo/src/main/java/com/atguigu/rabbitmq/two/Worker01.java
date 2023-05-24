package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author： tks
 * @date： 2022/7/20
 * @version： V1.0
 * 这是一个工作线程（相当于之前消费者）
 */
public class Worker01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        new Thread(new MyWork(), "线程1").start();
        new Thread(new MyWork(), "线程2").start();
    }

    static class MyWork implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + "正在等待。。。");
                Channel channel = RabbitMqUtils.getChannel();

                DeliverCallback deliverCallback = (consumerTag, message) -> {
                    System.out.println(Thread.currentThread().getName() + ">>接收到的消息:" + new String(message.getBody()));
                };

                CancelCallback cancelCallback = (messageTag) -> {
                    System.out.println(messageTag + "消费者取消消费接口回调逻辑");
                };

                /**
                 * 第一个参数：队列名称
                 * 第二个参数：自动签收
                 */
                channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
