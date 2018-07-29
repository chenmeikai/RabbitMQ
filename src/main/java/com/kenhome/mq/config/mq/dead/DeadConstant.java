package com.kenhome.mq.config.mq.dead;

/**
 * @Author: cmk
 * @Description: 常量
 * @Date: 2018\7\24 0024 22:57
 */
public class DeadConstant {

    //交换机名称
    public final static String EXCHANGE_NAME = "TEST_EXCHANGE";
    //队列
    public final static String QUEUE_NAME = "TEST_QUEUE";
    //路由key
    public final static String ROUNT_KEY_NAME = "TEST_KEY";


    //死信交换机
    public final static String EXCHANGE_DEAD_LETTER_NAME = "x-dead-letter-exchange";
    //死信队列
    public final static String QUEUE_DEAD_LETTER_NAME = "QUEUE_DEAD_LETTER_NAME";
    //死信路由key
    public final static String ROUNT_DEAD_LETTER_KEY = "x-dead-letter-routing-key";


    //死信队列转发队列
    public final static String QUEUE_REDIRECT_NAME = "QUEUE_REDIRECT_NAME";
    //死信转发路由key
    public final static String ROUNT_REDIRECT_KEY = "ROUNT_REDIRECT_KEY";


}
