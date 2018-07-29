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
 *
 * @author dax.
 * @version v1.0
 * @since 2018 /2/23 14:28
 */
@Configuration
public class DeadConfig {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Bean
    public AmqpTemplate amqpTemplate() {
        Logger log = LoggerFactory.getLogger(RabbitTemplate.class);
//          使用jackson 消息转换器
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

    /* ----------------------------------------------------------------------------测试--------------------------------------------------------------------------- */

    /**
     * @Description: 队列,支持持久化
     */
    @Bean("testQueue")
    public Queue ackQueue(){
        return QueueBuilder.durable(DeadConstant.QUEUE_NAME).build();
    }

    /**
     * @Description: 声明Direct交换机 支持持久化.
     */
    @Bean("testExchange")
    public Exchange ackExchange() {
        return ExchangeBuilder.directExchange(DeadConstant.EXCHANGE_NAME).durable(true).build();
    }

    /**
     * 通过绑定键 将指定队列绑定到一个指定的交换机
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding directBinding(@Qualifier("testQueue") Queue queue, @Qualifier("testExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DeadConstant.ROUNT_KEY_NAME).noargs();
    }


    /*----------------------------------------------------------------------------死信------------------------------------------------------------------------------*/

    /**
     * 死信队列跟交换机类型没有关系 不一定为directExchange  不影响该类型交换机的特性.
     *
     * @return the exchange
     */
    @Bean("deadLetterExchange")
    public Exchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(DeadConstant.EXCHANGE_DEAD_LETTER_NAME).durable(true).build();
    }

    /**
     * 声明一个死信队列.
     * x-dead-letter-exchange   对应  死信交换机
     * x-dead-letter-routing-key  对应 死信队列
     *
     * @return the queue
     */
    @Bean("deadLetterQueue")
    public Queue deadLetterQueue() {
        Map<String, Object> args = new HashMap<>(2);
//       x-dead-letter-exchange    声明  死信交换机
        args.put(DeadConstant.EXCHANGE_DEAD_LETTER_NAME, DeadConstant.EXCHANGE_DEAD_LETTER_NAME);
//       x-dead-letter-routing-key    声明 死信路由键
        args.put(DeadConstant.ROUNT_DEAD_LETTER_KEY, DeadConstant.ROUNT_DEAD_LETTER_KEY);
        return QueueBuilder.durable(DeadConstant.QUEUE_DEAD_LETTER_NAME).withArguments(args).build();
    }

    /**
     * 定义死信队列转发队列.
     *
     * @return the queue
     */
    @Bean("redirectQueue")
    public Queue redirectQueue() {
        return QueueBuilder.durable(DeadConstant.QUEUE_REDIRECT_NAME).build();
    }

    /**
     * 死信路由通过 DL_KEY 绑定键绑定到死信队列上.
     *
     * @return the binding
     */
    @Bean
    public Binding deadLetterBinding() {
        return new Binding(DeadConstant.QUEUE_DEAD_LETTER_NAME, Binding.DestinationType.QUEUE, DeadConstant.EXCHANGE_DEAD_LETTER_NAME, DeadConstant.ROUNT_DEAD_LETTER_KEY, null);

    }

    /**
     * 死信路由通过 KEY_R 绑定键绑定到死信队列上.
     *
     * @return the binding
     */
    @Bean
    public Binding redirectBinding() {
        return new Binding(DeadConstant.QUEUE_REDIRECT_NAME, Binding.DestinationType.QUEUE, DeadConstant.EXCHANGE_DEAD_LETTER_NAME, DeadConstant.ROUNT_REDIRECT_KEY, null);
    }
}
