package com.kenhome.mq.config.mq.direct;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DirectReceive {

    //监听器监听指定的Queue
    @RabbitListener(queues = DirectConstant.QUEUE_NAME)
    public void receive(String message) {

        System.out.println("接收到的信息是:" + message);


    }

}