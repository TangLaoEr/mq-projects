package com.example.springbootrabbitmq.consumer;

import com.example.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author： tks
 * @date： 2022/7/22
 * @version： V1.0
 */
@Slf4j
@Component
public class Consumer {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMessage(Message message) {
        String msg = new String(message.getBody());
        log.info("【正常】接受到的队列confirm.queue消息:{}", msg);
    }
}
