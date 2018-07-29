package com.kenhome.mq;


import com.kenhome.mq.config.mq.direct.DirectConstant;
import com.kenhome.mq.config.mq.topic.TopicConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: cmk
 * @Description:
 * @Date: 2018\7\24 0024 23:50
 */

public class TopicTest extends  MqApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

   @org.junit.Test
   public  void test(){
       //根据交换器和路由键发送消息
       rabbitTemplate.convertAndSend(TopicConstant.EXCHANGE_NAME,TopicConstant.ROUNT_KEY_ONE,"hello world");

       //根据交换器和路由键发送消息
       rabbitTemplate.convertAndSend(TopicConstant.EXCHANGE_NAME,TopicConstant.ROUNT_KEY_TWO,"hello world");

   }

}
