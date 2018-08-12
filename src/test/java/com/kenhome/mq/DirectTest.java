package com.kenhome.mq;


import com.kenhome.mq.config.mq.direct.DirectConstant;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: cmk
 * @Description:
 * @Date: 2018\7\24 0024 23:50
 */

public class DirectTest extends  MqApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @org.junit.Test
   public  void test(){
       //直接根据队列名发送消息
//       rabbitTemplate.convertAndSend(DirectConstant.QUEUE_NAME,"hello world");
       //根据交换器和路由键发送消息
       for(int i=0;i<100;i++){
           rabbitTemplate.convertAndSend(DirectConstant.EXCHANGE_NAME,DirectConstant.ROUNT_KEY_NAME,"number:"+i);
       }
   }

}
