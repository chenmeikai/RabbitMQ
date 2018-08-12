package com.kenhome.mq.config.mq.fanout;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author: cmk
 * @Description: fanout(广播) 模式
 * @Date: 2018\7\28 0028 11:13
 */
//@Configuration
public class FanoutConfig {

    /**
     * 队列
     */
    @Bean("fanout-queue-one")
    public Queue queueFanoutOne() {
        return new Queue(FanoutConstant.QUEUE_NAME_ONE, true);
    }

    /**
     * 队列
     */
    @Bean("fanout-queue-two")
    public Queue queueFanoutTwo() {
        return new Queue(FanoutConstant.QUEUE_NAME_TWO, true);
    }


    /**
     * 声明fanout交换机
     *
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return  new FanoutExchange(FanoutConstant.EXCHANGE_NAME);
    }


    /**
     * 绑定
     */
    @Bean
    public Binding fanoutBindOne() {
        return BindingBuilder.bind(queueFanoutOne()).to(fanoutExchange());
    }
    /**
     * 绑定
     */
    @Bean
    public Binding fanoutBindTwo() {
        return BindingBuilder.bind(queueFanoutTwo()).to(fanoutExchange());
    }





}
