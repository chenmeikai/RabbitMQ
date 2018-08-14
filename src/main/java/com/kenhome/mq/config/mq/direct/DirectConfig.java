package com.kenhome.mq.config.mq.direct;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: cmk
 * @Description: direct模式
 * @Date: 2018\7\24 0024 23:50
 */
//@Configuration
public class DirectConfig {

    /**
     * 声明队列
     *
     * @return
     */
    @Bean
    public Queue directQueue() {
        // true表示持久化该队列
        return new Queue(DirectConstant.QUEUE_NAME, true);
    }

    /**
     * 声明direct交互器
     *
     * @return
     */
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(DirectConstant.EXCHANGE_NAME);
    }

    /**
     * 绑定
     *
     * @return
     */
    @Bean
    public Binding directBind() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with(DirectConstant.ROUNT_KEY_NAME);
    }

}
