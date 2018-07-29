package com.kenhome.mq;


import com.kenhome.mq.config.mq.fanout.FanoutConstant;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: cmk
 * @Description:
 * @Date: 2018\7\24 0024 23:50
 */

public class FanoutTest extends  MqApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;


   @org.junit.Test
   public  void test(){
       //根据交换器和路由键发送消息
       rabbitTemplate.convertAndSend(FanoutConstant.EXCHANGE_NAME,"","hello world !!");
   }

}
