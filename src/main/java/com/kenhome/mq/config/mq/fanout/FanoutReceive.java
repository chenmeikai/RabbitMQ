package com.kenhome.mq.config.mq.fanout;

import com.kenhome.mq.config.mq.topic.TopicConstant;
import org.springframework.amqp.core.Message;
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
    public void receiveOne(Message message) {
        System.out.println(FanoutConstant.QUEUE_NAME_ONE + "1接收到的信息是:" + new String (message.getBody()));
    }


    @RabbitListener(queues = FanoutConstant.QUEUE_NAME_TWO)
    public void receiveTwo(Message message) {
        System.out.println(FanoutConstant.QUEUE_NAME_TWO+"2接收到的信息是:" + new String (message.getBody()));
    }
}
