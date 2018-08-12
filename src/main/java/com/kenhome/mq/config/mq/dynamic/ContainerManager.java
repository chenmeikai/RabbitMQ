package com.kenhome.mq.config.mq.dynamic;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author: cmk
 * @Description: 管理容器
 * @Date: 2018\8\12 0012 21:12
 */

@Component
public class ContainerManager {

    @Resource
    private Map<String,SimpleMessageListenerContainer> messageContainers;
    @Resource
    private ConnectionFactory connectionFactory;


   /**
    * @Description: 添加队列和消费者
    * @param: [queueName, exchangeName, routingKey, receiver]
    * @return: void
    */
    public void addQueue(String queueName, String exchangeName, String routingKey, ChannelAwareMessageListener receiver){

        RabbitAdmin admin = new RabbitAdmin(connectionFactory);

        //创建队列
        Queue queue =new Queue(queueName);
        admin.declareQueue(queue);
        //创建exchange
        DirectExchange exchange = new DirectExchange(exchangeName);
        admin.declareExchange(exchange);
        //绑定
        admin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(routingKey));
        //消费者
       /* DynamicReceive dynamicReceive = new DynamicReceive();*/
        MessageListenerAdapter adapter = new MessageListenerAdapter(receiver);
        //配置容器
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames(queueName);
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        //消息确认后才能删除
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //每次处理1条消息
        container.setPrefetchCount(1);
        container.setMessageListener(adapter);
        container.start();
        messageContainers.put(queueName,container);
    }

    /**
     * @Description: 暂停队列
     * @param: [queueName]
     * @return: boolean
     */
    public boolean stopQueue(String queueName){

        boolean flag =false;

        SimpleMessageListenerContainer simpleMessageListenerContainer = messageContainers.get(queueName);

        if(simpleMessageListenerContainer !=null){
            simpleMessageListenerContainer.stop();
            flag=true;
        }
        return  flag;
    }

    /**
     * @Description: 启动队列
     * @param: [queueName]
     * @return: boolean
     */
    public boolean startQueue(String queueName){

        boolean flag =false;

        SimpleMessageListenerContainer simpleMessageListenerContainer = messageContainers.get(queueName);

        if(simpleMessageListenerContainer !=null){
            simpleMessageListenerContainer.start();
            flag=true;
        }
        return  flag;
    }


}
