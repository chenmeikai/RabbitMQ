package com.kenhome.mq.config.mq.dead;

/**
 * @Author: cmk
 * @Description: 常量
 * @Date: 2018\7\24 0024 22:57
 */
public class DeadConstant {

    //交换机名称
    public final static String EXCHANGE_NAME_ONE = "EXCHANGE_NAME_ONE";
    //队列
    public final static String QUEUE_NAME_ONE = "QUEUE_NAME_ONE";
    //路由key
    public final static String ROUNT_KEY_ONE = "ROUNT_KEY_ONE";

    //死信交换机
    public final static String EXCHANGE_DEAD_LETTER_NAME = "EXCHANGE_DEAD_LETTER_NAME";
    //死信路由key
    public final static String ROUNT_DEAD_LETTER_KEY = "ROUNT_DEAD_LETTER_KEY";
    //死信队列转发队列
    public final static String QUEUE_REDIRECT_NAME = "QUEUE_REDIRECT_NAME";


}
