package com.example.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author： tks
 * @date： 2022/7/22
 * @version： V1.0
 */
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    // 注入
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * PostConstruct的顺序在Autowired之后
     */
    @PostConstruct
    public void init() {
        System.out.println("注入");
        // 这里一定要记得注入
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     *
     * 交换机确认回调方法
     * 1.发消息,交换机接收到消息,回调
     *
     * 2.发消息,交换机接收失败了,回调
     *
     * @param correlationData 保存回调消息的ID及相关信息
     * @param ack 交换机收到消息 true | 没有接收到消息 false
     * @param cause cause null | 没有接收到消息的异常消息
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";

        if (ack) {
            // 交换机接收到消息
            log.info("[交换机]已经收到Id为:{}的消息", id);
        }else {
            // 交换机没有接收到消息
            log.info("[交换机]还未收到Id为:{}的消息, 由于原因:{}", id, cause);
        }
    }

    /**
     * 可以在当消息传递过程中不可达目的地时将消息返回给生产者
     * 只有不可达目的地的时候，才进行回退
     * @param returnedMessage
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("【回退】消息{}，被交换机{}，退回，退回原因：{}，路由key:{}",
                returnedMessage.getMessage(),
                returnedMessage.getExchange(),
                returnedMessage.getReplyText(),
                returnedMessage.getRoutingKey());
    }
}
