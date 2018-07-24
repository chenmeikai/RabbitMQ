package com.kenhome.mq;


import com.kenhome.mq.config.mq.RabbitConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: cmk
 * @Description:
 * @Date: 2018\7\24 0024 23:50
 */

public class Test extends  MqApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

   @org.junit.Test
   public  void test(){
       rabbitTemplate.convertAndSend(RabbitConstant.QUEUE_TRANSACTION,"hello world");
   }

}
