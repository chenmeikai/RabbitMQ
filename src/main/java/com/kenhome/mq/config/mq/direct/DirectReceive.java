package com.kenhome.mq.config.mq.direct;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DirectReceive {

    Logger log = LoggerFactory.getLogger(DirectReceive.class);

    //监听器监听指定的Queue
    @RabbitListener(queues = DirectConstant.QUEUE_NAME)
    public void redirect(Message message, Channel channel) throws IOException {
        log.info("message1消息 {}", new String(message.getBody()));
    }


    //监听器监听指定的Queue
    @RabbitListener(queues = DirectConstant.QUEUE_NAME)
    public void redirect2(Message message, Channel channel) throws IOException {
        log.info("message2消息 {}", new String(message.getBody()));
        for (int i = 0; i < 200; i++) {

        }
    }

}