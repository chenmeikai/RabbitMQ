package com.kenhome.mq;


import com.kenhome.mq.config.mq.ack.AckConstant;
import com.kenhome.mq.config.mq.direct.DirectConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

/**
 * @Author: cmk
 * @Description:
 * @Date: 2018\7\24 0024 23:50
 */

public class AckTest extends  MqApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @org.junit.Test
   public  void test(){
        String message = "我是message";
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(AckConstant.EXCHANGE_NAME, AckConstant.ROUNT_KEY_NAME, message, correlationData);
        System.out.println("成功");
        for (int i=0;i<10;i++){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
