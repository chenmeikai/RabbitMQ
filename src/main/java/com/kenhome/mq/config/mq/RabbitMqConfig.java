package com.kenhome.mq.config.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
 
    /**
     * 声明队列
     *
     * @return
     */
    @Bean
    public Queue queueTransaction() {
        // true表示持久化该队列
        return new Queue(RabbitConstant.QUEUE_TRANSACTION, true);
    }
 

    /**
     * 声明交互器
     *
     * @return
     */
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(RabbitConstant.EXCHANGE);
    }
 
    /**
     * 绑定
     *
     * @return
     */
    @Bean
    public Binding bindingTransaction() {
        return BindingBuilder.bind(queueTransaction()).to(directExchange()).with(RabbitConstant.RK_TRANSACTION);
    }
 
}
