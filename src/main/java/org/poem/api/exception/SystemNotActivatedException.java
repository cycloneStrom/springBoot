package org.poem.api.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by poem on 2016/7/2.
 * 系统激活管理
 */
public class SystemNotActivatedException extends AuthenticationException {

    /**
     * 系统激活异常
     * @param message 消息体
     */
    public SystemNotActivatedException(String message) {
        super(message);
    }

    /**
     * 系统激活异常
     * @param message 消息
     * @param t 栈队
     */
    public SystemNotActivatedException(String message, Throwable t) {
        super(message, t);
    }
}