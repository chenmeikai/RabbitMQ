package com.kenhome.mq.config.mq.dynamic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: cmk
 * @Description: 动态配置
 * @Date: 2018\7\28 0028 23:01
 */
@Configuration
public class DynamicConfig {

    private final static Logger logger = LoggerFactory.getLogger(DynamicConfig.class);

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Bean
    public AmqpTemplate amqpTemplate() {
        Logger log = LoggerFactory.getLogger(RabbitTemplate.class);
        //使用jackson 消息转换器
        /*rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());*/
        rabbitTemplate.setEncoding("UTF-8");
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            String correlationId = message.getMessageProperties().getCorrelationId();
            log.info("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", correlationId, replyCode, replyText, exchange, routingKey);
        });
        /* 消息确认 需要配置   publisher-returns: true */
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            try {
                if (ack) {
                    log.info("消息发送到exchange成功,id: {}", correlationData.getId());
                } else {
                    log.info("消息发送到exchange失败,原因: {}", cause);
                }
            } catch (Exception e) {
                log.error("消息发送到exchange失败");
                e.printStackTrace();

            }
        });
        return rabbitTemplate;
    }


    //配置队列与消费 ，可管理messageContainers
    @Bean
    public Map<String, SimpleMessageListenerContainer> messageContainers(ConnectionFactory connectionFactory) throws Exception {

        Map<String, SimpleMessageListenerContainer> messageContainers = new HashMap<>(16);

        RabbitAdmin admin = new RabbitAdmin(connectionFactory);

        //创建队列
        Queue queue = new Queue("moveQueue");
        admin.declareQueue(queue);
        //创建exchange
        DirectExchange exchange = new DirectExchange("moveExchange");
        admin.declareExchange(exchange);
        //绑定
        admin.declareBinding(BindingBuilder.bind(queue).to(exchange).with("moveKey"));
        //消费者
        DynamicReceive dynamicReceive = new DynamicReceive();
        MessageListenerAdapter adapter = new MessageListenerAdapter(dynamicReceive);
        //配置容器
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames("moveQueue");
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        //消息确认后才能删除
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //每次处理1条消息
        container.setPrefetchCount(1);
        container.setMessageListener(adapter);
        container.start();
        messageContainers.put("moveQueue", container);

        /*2*/
        //创建队列
        Queue queue2 = new Queue("moveQueue2");
        admin.declareQueue(queue2);
        //创建exchange
        DirectExchange exchange2 = new DirectExchange("moveExchange2");
        admin.declareExchange(exchange2);
        //绑定
        admin.declareBinding(BindingBuilder.bind(queue2).to(exchange2).with("moveKey2"));
        //消费者
        DynamicReceive dynamicReceive2 = new DynamicReceive();
        MessageListenerAdapter adapter2 = new MessageListenerAdapter(dynamicReceive2);
        //配置容器
        SimpleMessageListenerContainer container2 = new SimpleMessageListenerContainer(connectionFactory);
        container2.setQueueNames("moveQueue2");
        container2.setExposeListenerChannel(true);
        container2.setMaxConcurrentConsumers(1);
        container2.setConcurrentConsumers(1);
        //消息确认后才能删除
        container2.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //每次处理1条消息
        container2.setPrefetchCount(1);
        container2.setMessageListener(adapter2);
        container2.start();
        messageContainers.put("moveQueue2", container2);


        return messageContainers;
    }

}
