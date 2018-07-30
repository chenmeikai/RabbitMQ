package com.kenhome.mq.config.mq.dead;

import com.kenhome.mq.config.mq.ack.AckConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 队列配置.
 * @author cmk
 * @version v1.0
 * @since 2018 /7/30 20:28
 */
@Configuration
public class DeadConfig {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Bean
    public AmqpTemplate amqpTemplate() {
        Logger log = LoggerFactory.getLogger(RabbitTemplate.class);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setEncoding("UTF-8");
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            String correlationId = message.getMessageProperties().getCorrelationId();
            log.debug("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", correlationId, replyCode, replyText, exchange, routingKey);
        });
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.debug("消息发送到exchange成功,id: {}", correlationData.getId());
            } else {
                log.debug("消息发送到exchange失败,原因: {}", cause);
            }
        });
        return rabbitTemplate;
    }

    /*----------------------------------------------------------------------------死信------------------------------------------------------------------------------*/

    /**
     * 死信队列交换机
     * @return the exchange
     */
    @Bean("deadLetterExchange")
    public Exchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(DeadConstant.EXCHANGE_DEAD_LETTER_NAME).durable(true).build();
    }

    /**
     * 声明一个队列.
     * x-dead-letter-exchange   配置死信交换机
     * x-dead-letter-routing-key 配置死信路由key
     * @return the queue
     */
    @Bean("queue_name_one")
    public Queue deadLetterQueue() {
        Map<String, Object> args = new HashMap<>(2);
        // x-dead-letter-exchange    声明  死信交换机
        args.put("x-dead-letter-exchange", DeadConstant.EXCHANGE_DEAD_LETTER_NAME);
        //x-dead-letter-routing-key    声明 死信路由键
        args.put("x-dead-letter-routing-key", DeadConstant.ROUNT_DEAD_LETTER_KEY);
        return QueueBuilder.durable(DeadConstant.QUEUE_NAME_ONE).withArguments(args).build();
    }

    /**
     * 死信转发队列.
     * @return the queue
     */
    @Bean("redirect_queue")
    public Queue redirectQueue() {
        return QueueBuilder.durable(DeadConstant.QUEUE_REDIRECT_NAME).build();
    }
    /**
     * 绑定转发队列+死信exchange+路由key
     * @return the binding
     */
    @Bean
    public Binding deadLetterBinding() {
        return new Binding(DeadConstant.QUEUE_REDIRECT_NAME, Binding.DestinationType.QUEUE, DeadConstant.EXCHANGE_DEAD_LETTER_NAME, DeadConstant.ROUNT_DEAD_LETTER_KEY, null);
    }

    /**
     * 执行普通的绑定,交换机可以和死信交换机相同
     * @return the binding
     */
    @Bean
    public Binding redirectBinding() {
        return new Binding(DeadConstant.QUEUE_NAME_ONE, Binding.DestinationType.QUEUE, DeadConstant.EXCHANGE_DEAD_LETTER_NAME, DeadConstant.ROUNT_KEY_ONE, null);
    }
}
