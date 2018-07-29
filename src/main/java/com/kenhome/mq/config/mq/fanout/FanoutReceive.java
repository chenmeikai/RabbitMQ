package com.kenhome.mq.config.mq.fanout;

import com.kenhome.mq.config.mq.topic.TopicConstant;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: cmk
 * @Description:
 * @Date: 2018\7\27 0027 22:36
 */
@Component
public class FanoutReceive {

    //监听器监听指定的Queue
    @RabbitListener(queues = FanoutConstant.QUEUE_NAME_ONE)
    public void receiveOne(String message) {
        System.out.println(FanoutConstant.QUEUE_NAME_ONE+"接收到的信息是:" + message);
    }

    @RabbitListener(queues = FanoutConstant.QUEUE_NAME_TWO)
    public void receiveTwo(String message) {
        System.out.println(FanoutConstant.QUEUE_NAME_TWO+"接收到的信息是:" + message);
    }
}
