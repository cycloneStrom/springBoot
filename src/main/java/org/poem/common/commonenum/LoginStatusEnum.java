package org.poem.common.commonenum;

/**
 * Created by poem on 2016/6/18.
 * 登录状态的管理
 */
public enum LoginStatusEnum {

    /**
     * 登录成功
     */
    SUCCESS(0, "Authentication successful!"),
    /**
     * 登录失败
     */
    FAILED(1, "Authentication failed!"),
    /**
     * 激活失败
     */
    ACTIVATE(2, "Activated failed!");

    private int code;
    private String message;

    LoginStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

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
}
