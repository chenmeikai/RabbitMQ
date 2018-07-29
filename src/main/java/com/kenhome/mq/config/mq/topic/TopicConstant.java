package com.kenhome.mq.config.mq.topic;

/**
 * @Author: cmk
 * @Description:常量
 * @Date: 2018\7\27 0027 21:43
 */
public class TopicConstant {

    //交换机名称
    public final static String EXCHANGE_NAME = "TOPIC_EXCHANGE";
    //队列1
    public final static String QUEUE_NAME_ONE = "TOPIC_QUEUE_ONE";
    //队列2
    public final static String QUEUE_NAME_TWO = "TOPIC_QUEUE_TWO";
    //路由key1
    public final static String ROUNT_KEY_ONE = "topicOne.topicTwo";
    //路由key2
    public final static String ROUNT_KEY_TWO = "topicOne.hello";

    //绑定key
    public final static String BIND_KEY_ONE = "topicOne.topicTwo";
    //绑定key //*表示任意一个词,#表示零个或多个词
    public final static String BIND_KEY_TWO = "topicOne.#";
}
