package org.poem.common.utils;

import java.io.Serializable;

/**
 * Created by lyw on 2016/4/14.
 * 前后端交互的信息实体
 */
public class JsonBean<T> implements Serializable {

    /**
     * 异常提示信息.
     */
    private String message = "操作成功";

    /**
     * 状态.
     */
    private int status = 0;

    /**
     * 结果对象.
     */
    private T result;

    /**
     * Instantiates a new json bean.
     */
    public JsonBean() {
    }

    /**
     * Instantiates a new json bean.
     *
     * @param result the result
     */
    public JsonBean(T result) {
        this.result = result;
    }

    /**
     * Instantiates a new json bean.
     *
     * @param message the message
     * @param status  the status
     */
    public JsonBean(String message, int status) {
        this.message = message;
        this.status = status;
    }

    /**
     * Instantiates a new json bean.
     *
     * @param message the message
     * @param status  the status
     * @param result  the result
     */
    public JsonBean(String message, int status, T result) {
        this.message = message;
        this.status = status;
        this.result = result;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Gets the result.
     *
     * @return the result
     */
    public T getResult() {
        return result;
    }

    /**
     * Sets the result.
     *
     * @param result the new result
     */
    public void setResult(T result) {
        this.result = result;
    }
}
