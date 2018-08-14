package com.kenhome.mq.config.mq.dynamic;


import com.kenhome.mq.config.mq.ack.Operation;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;


/**
 * @Author: cmk
 * @Description:
 * @Date: 2018\7\28 0028 23:25
 */

public class DynamicReceive implements ChannelAwareMessageListener {

    private static final Logger log = LoggerFactory.getLogger(DynamicReceive.class);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        long tag = message.getMessageProperties().getDeliveryTag();
        Operation operation = Operation.ACCEPT;
        try {
            // 如果成功完成则action=Action.ACCEPT
            log.info("动态获得的消息是：" + new String(message.getBody()));
            if ("a5".equals(new String(message.getBody()))) {
                Thread.sleep(3000);
            }
        } catch (Exception e) {
            //TODO 根据异常决定处理策略
            operation = com.kenhome.mq.config.mq.ack.Operation.REJECT;
            e.printStackTrace();
        } finally {
            if (operation == com.kenhome.mq.config.mq.ack.Operation.ACCEPT) {
                channel.basicAck(tag, true);
            } else if (operation == Operation.RETRY) {//重入队列
                channel.basicNack(tag, false, true);
            } else {//丢弃
                channel.basicNack(tag, false, false);
            }
        }
    }
}
