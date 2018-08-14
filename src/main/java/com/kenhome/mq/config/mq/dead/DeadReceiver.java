package com.kenhome.mq.config.mq.dead;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 监听器.
 *
 * @author dax.
 * @version v1.0
 * @since 2018 /2/24 9:36
 */
@Component
public class DeadReceiver {
    private static final Logger log = LoggerFactory.getLogger(DeadReceiver.class);


    /**
     * 接收死信的消费者
     *
     * @param message the message
     * @param channel the channel
     * @throws IOException the io exception  这里异常需要处理
     */
    @RabbitListener(queues = {DeadConstant.QUEUE_REDIRECT_NAME})
    public void redirect(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        log.info("dead message消息 {}", new String(message.getBody()));
    }
}
