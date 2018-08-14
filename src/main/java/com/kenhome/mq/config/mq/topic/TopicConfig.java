package com.kenhome.mq.config.mq.topic;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author: cmk
 * @Description: topic(主题)模式
 * @Date: 2018\7\27 0027 21:34
 */
//@Configuration
public class TopicConfig {


    /**
     * 声明队列
     *
     * @return
     */
    @Bean("topic-queue-one")
    public Queue queueOne() {
        // true表示持久化该队列
        return new Queue(TopicConstant.QUEUE_NAME_ONE, true);
    }

    @Bean("topic-queue-two")
    public Queue queueTwo() {
        // true表示持久化该队列
        return new Queue(TopicConstant.QUEUE_NAME_TWO, true);
    }

    /**
     * 声明topic交换机
     *
     * @return
     */
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(TopicConstant.EXCHANGE_NAME);
    }


    /**
     * 绑定
     *
     * @return
     */
    @Bean
    public Binding topicBindOne() {
        return BindingBuilder.bind(queueOne()).to(topicExchange()).with(TopicConstant.BIND_KEY_ONE
        );
    }

    @Bean
    public Binding topicBindTwo() {
        return BindingBuilder.bind(queueTwo()).to(topicExchange()).with(TopicConstant.BIND_KEY_TWO
        );
    }


}
