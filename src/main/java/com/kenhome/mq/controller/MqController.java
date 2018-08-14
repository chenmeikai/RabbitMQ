package com.kenhome.mq.controller;

import com.kenhome.mq.config.mq.ack.AckConstant;
import com.kenhome.mq.config.mq.direct.DirectConstant;
import com.kenhome.mq.config.mq.dynamic.ContainerManager;
import com.kenhome.mq.config.mq.dynamic.DynamicReceive2;
import com.kenhome.mq.config.mq.fanout.FanoutConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Author: cmk
 * @Description:
 * @Date: 2018\8\5 0005 10:52
 */

@RestController
public class MqController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ContainerManager containerManager;

    @GetMapping("direct")
    public String direct() {

        //根据交换器和路由键发送消息
        for (int i = 0; i < 100; i++) {
            rabbitTemplate.convertAndSend(DirectConstant.EXCHANGE_NAME, DirectConstant.ROUNT_KEY_NAME, "number:" + i);
        }
        return "success";
    }

    @GetMapping("fanout")
    public String fanout() {

        //根据交换器和路由键发送消息
        for (int i = 0; i < 100; i++) {
            rabbitTemplate.convertAndSend(FanoutConstant.EXCHANGE_NAME, null, "number:" + i);
        }
        return "success";
    }


    @GetMapping("ack")
    public String ack() {

        System.out.println("成功");
        for (int i = 0; i < 50; i++) {
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend(AckConstant.EXCHANGE_NAME, AckConstant.ROUNT_KEY_NAME, i, correlationData);
        }

        return "success";
    }

    @GetMapping("move")
    public String move() {

        System.out.println("成功");
        for (int i = 0; i < 50; i++) {
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend("moveExchange", "moveKey", "a" + i, correlationData);
        }

        return "success";
    }

    @GetMapping("move2")
    public String move2() {

        System.out.println("成功");
        for (int i = 0; i < 50; i++) {
            CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend("moveExchange2", "moveKey2", "b" + i, correlationData);
        }
        return "success";
    }


    @GetMapping("add")
    public String add() {
        System.out.println("成功");
        String queueName = "addQueue";
        String exchangeName = "addExchange";
        String routingKey = "addKey";
        DynamicReceive2 dynamicReceive2 = new DynamicReceive2();
        containerManager.addQueue(queueName, exchangeName, routingKey, dynamicReceive2);
        return "success";
    }


    @GetMapping("testAdd")
    public String testAdd() {
        System.out.println("成功");
        String queueName = "addQueue";
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("addExchange", "addKey", "动态添加队列成功", correlationData);
        return "success";
    }


    @GetMapping("stop")
    public boolean stop() {
        System.out.println("成功");
        String queueName = "addQueue";
        containerManager.stopQueue(queueName);
        return containerManager.stopQueue(queueName);
    }

    @GetMapping("start")
    public boolean start() {
        System.out.println("成功");
        String queueName = "addQueue";
        containerManager.stopQueue(queueName);
        return containerManager.startQueue(queueName);
    }

}
