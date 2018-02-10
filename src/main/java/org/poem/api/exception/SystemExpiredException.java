package org.poem.api.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by poem on 2016/7/2.
 * 系统运行错误异常
 */
public class SystemExpiredException extends AuthenticationException {

    /**
     * 系统运行错误 消息队列
     * @param message 消息队列
     */
    public SystemExpiredException(String message) {
        super(message);
    }

    /**
     * 系统运行异常  消息队列
     * @param message 消息
     * @param t 线程
     */
    public SystemExpiredException(String message, Throwable t) {
        super(message, t);
    }
}
