package com.example.springbootrabbitmq.consumer;

import com.example.springbootrabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 消费者、基于插件的延迟消息
 * @author： tks
 * @date： 2022/7/22
 * @version： V1.0
 */
@Slf4j
@Component
public class DelayQueueConsumer {
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelayQueue(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间:{}, 收到延迟队列的消息:{}", new Date().toString(), msg);
    }
}
