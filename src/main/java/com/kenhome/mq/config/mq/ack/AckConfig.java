package com.kenhome.mq.config.mq.ack;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author: cmk
 * @Description: 确认机制
 * @Date: 2018\7\28 0028 23:01
 */
//@Configuration
public class AckConfig {

    private final static Logger logger = LoggerFactory.getLogger(AckConfig.class);

    @Resource
    private RabbitTemplate rabbitTemplate;


    @Bean
    public SimpleRabbitListenerContainerFactory myContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //设置预取数量为1，公平分发
        factory.setPrefetchCount(1);
        configurer.configure(factory, connectionFactory);
        return factory;
    }


    @Bean
    public AmqpTemplate amqpTemplate() {
        Logger log = LoggerFactory.getLogger(RabbitTemplate.class);
        //使用jackson 消息转换器
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
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

    /**
     * @Description: 队列, 支持持久化
     */
    @Bean("ackQueue")
    public Queue ackQueue() {
        return QueueBuilder.durable(AckConstant.QUEUE_NAME).build();
    }


    /**
     * @Description: 声明Direct交换机 支持持久化.
     */
    @Bean("ackExchange")
    public Exchange ackExchange() {
        return ExchangeBuilder.directExchange(AckConstant.EXCHANGE_NAME).durable(true).build();
    }


    /**
     * 通过绑定键 将指定队列绑定到一个指定的交换机
     *
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding directBinding(@Qualifier("ackQueue") Queue queue, @Qualifier("ackExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(AckConstant.ROUNT_KEY_NAME).noargs();
    }


    //可动态配置
    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) throws Exception {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);

        Channel channel = connectionFactory.createConnection().createChannel(false);

        //创建队列
        channel.queueDeclare("moveQueue", true, false, false, null);
        //创建exchange
        channel.exchangeDeclare("moveExchange", BuiltinExchangeType.DIRECT, true);
        //绑定通过 exchange、队列
        channel.queueBind("moveQueue", "moveExchange", "moveKey");


        container.setQueueNames("moveQueue", "moveQueue2");
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        //消息确认后才能删除
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //每次处理5条消息
        container.setPrefetchCount(1);
        container.setMessageListener(new ChannelAwareMessageListener() {

            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                byte[] body = message.getBody();
                System.out.println("可动态配置消费者接收到的消息: " + new String(body));
                String num = new String(body);
                if ("\"a5\"".equals(num)) {
                    Thread.sleep(5000);
                }
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        });
        return container;
    }

}
