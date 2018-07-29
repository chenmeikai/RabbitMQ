package com.kenhome.mq.config.mq.ack;


import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @Author: cmk
 * @Description:
 * @Date: 2018\7\28 0028 23:25
 */
@Component
public class AckReceive {

    private final static Logger log = LoggerFactory.getLogger(AckReceive.class);

    /**
     * 确认机制
     * @param message the message
     * @param channel the channel
     */
    @RabbitListener(queues = AckConstant.QUEUE_NAME)
    public void message(Message message, Channel channel) throws IOException {

        long tag = message.getMessageProperties().getDeliveryTag();
        Operation operation = Operation.ACCEPT;
        try {
            // 如果成功完成则action=Action.ACCEPT
            log.info("获得的消息是：" + new String(message.getBody()));
        } catch (Exception e) {
            //TODO 根据异常决定处理策略
            e.printStackTrace();
        } finally {
            if (operation == Operation.ACCEPT) {
                channel.basicAck(tag, true);
            } else if (operation == Operation.RETRY) {
                channel.basicNack(tag, false, true);
            } else {
                channel.basicNack(tag, false, false);
            }
        }

    }
}
