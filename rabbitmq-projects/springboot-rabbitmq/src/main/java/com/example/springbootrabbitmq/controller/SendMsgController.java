package com.example.springbootrabbitmq.controller;

import com.example.springbootrabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author： tks
 * @date： 2022/7/22
 * @version： V1.0
 */
@Slf4j
@RestController
@RequestMapping(value = "/ttl")
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 基于插件的延迟消息
     */
    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable("message") String message,
                        @PathVariable("delayTime") Integer delayTime) {
        log.info("当前时间:{},发送一条时长{}毫秒信息给延迟队列delayed.queue:{}", new Date().toString(), delayTime, message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME, DelayedQueueConfig.DELAYED_ROUTING_KEY,
                message, message1 -> {
                    // 发送消息的时候，延迟时长，单位:ms
                    message1.getMessageProperties().setDelay(delayTime);
                    return message1;
                });
    }
}
