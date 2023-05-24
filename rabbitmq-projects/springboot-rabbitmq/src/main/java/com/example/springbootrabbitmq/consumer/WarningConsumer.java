package com.example.springbootrabbitmq.consumer;

import com.example.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 报警消费
 * @author： tks
 * @date： 2022/7/22
 * @version： V1.0
 */
@Component
@Slf4j
public class WarningConsumer {

    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message) {
        String msg = new String(message.getBody());
        log.error("【报警】发现不可路由消息:{}", msg);
    }
}
