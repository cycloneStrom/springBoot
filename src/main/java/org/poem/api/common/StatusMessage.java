package org.poem.api.common;

import java.io.Serializable;

/**
 * Created by poem on 2016/5/6.
 * 错误代码
 */
public enum StatusMessage implements Serializable{
    /**
     * 初始化
     */
    COMMON_ERROR(0,"");

    /**
     * 错误代码
     */
    private int code;

    /**
     * 错误原因
     */
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


     StatusMessage() {
    }

     StatusMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
