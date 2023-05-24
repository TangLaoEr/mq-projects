package com.atguigu.rabbitmq.four;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * 异步确认发布
 */
public class AsyncConfirmMessage {
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

        /**
         * 线程安全有序的一个哈希表，适用于高并发情况下
         * 1.轻松的将序号与消息进行关联
         * 2.轻松批量删除条目,只要给到序号
         * 3.支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        /**
         * 消息确认成功、回调函数
         */
        ConfirmCallback confirmCallback = new ConfirmCallback() {
            @Override
            public void handle(long no, boolean multiple) throws IOException {
                // 如果是批量
                if (multiple) {
                    ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(no);
                    confirmed.clear();
                }else {
                    outstandingConfirms.remove(no);
                }

                System.out.println("消息发布成功、确认通知" + no);
            }
        };

        /**
         * 消息确认失败，回调函数
         * 1.消息的标记
         * 2.是否为批量确认
         */
        ConfirmCallback nackCallback = (no, multiple) -> {
            String message = outstandingConfirms.get(no);
            System.out.println("未确认的消息是:" + message + "::::未确认的消息tag:" + no);
        };

        /**
         * 准备消息的监听器、监听哪些消息成功了、哪些消息失败了
         * 消息发布的回调机制
         */
        channel.addConfirmListener(confirmCallback, nackCallback);

        // 开始时间
        long beginTime = System.currentTimeMillis();
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            // 此处记录下所有要发送的消息，消息的总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步发布确认消息，耗时" + (endTime - beginTime));
    }

    // 异步确认
    public static void publishMessageAsync() throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);

        // 开启发布确认
        channel.confirmSelect();

        /**
         * 线程安全有序的一个哈希表，适用于高并发的情况
         * 1.轻松的将序号与消息进行关联
         * 2.轻松批量删除条目 只要给到序列号
         * 3.支持并发访问
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new
                ConcurrentSkipListMap<>();

        ConfirmCallback ackCallback = (sequenceNumber, multiple) -> {
            if (multiple) {
                //返回的是小于等于当前序列号的未确认消息 是一个 map
                ConcurrentNavigableMap<Long, String> confirmed =
                        outstandingConfirms.headMap(sequenceNumber, true);

                // 清除该部分未确认消息
                confirmed.clear();
            }else{
                outstandingConfirms.remove(sequenceNumber);
            }
        };

        ConfirmCallback nackCallback = (sequenceNumber, multiple) -> {
            String message = outstandingConfirms.get(sequenceNumber);
            System.out.println(message + "消息未被确认");
        };

        /**
         * 添加一个异步确认的监听器
         * 1.确认收到消息的回调
         * 2.未收到消息的回调
         */
        channel.addConfirmListener(ackCallback, nackCallback);

        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息" + i;
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
            channel.basicPublish("", queueName, null, message.getBytes());
        }
    }
}
