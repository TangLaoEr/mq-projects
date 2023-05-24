package com.atguigu.rabbitmq.exchange;

import com.atguigu.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author： tks
 * @date： 2022/7/20
 */
public class FanoutReceiveLogs01 {

    public static final String EXCHANGE_NAME = "logs";

    public static final String FILE_PATH = "E:\\temp\\rabbitmq_info.txt";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("等待接收消息，把接收到的消息写到文件...");


        DeliverCallback deliverCallback = (s, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            File file = new File(FILE_PATH);
            FileUtils.writeStringToFile(file, message, "UTF-8");
            System.out.println("数据写入文件成功");
        };

        channel.basicConsume(queueName, true, deliverCallback, s -> {});
    }
}
