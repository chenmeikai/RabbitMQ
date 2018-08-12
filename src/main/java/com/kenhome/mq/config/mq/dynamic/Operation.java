package com.kenhome.mq.config.mq.dynamic;

/**
 * @Author: cmk
 * @Description: 消息处理策略
 * @Date: 2018\7\29 0029 21:58
 */
public enum Operation {

    /**
     * @Description: 处理成功
     */
    ACCEPT,
    /**
     * @Description: 重放队列中
     */
    RETRY,
    /**
     * @Description: 丢弃
     */
    REJECT,
}
