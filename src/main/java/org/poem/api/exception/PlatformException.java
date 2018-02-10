package org.poem.api.exception;

import org.poem.api.common.StatusMessage;
import org.springframework.util.StringUtils;

/**
 * Created by poem on 2016/5/6.
 * 异常处理
 */
public class PlatformException extends Exception {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 883368829481007561L;
    /**
     * 消息体
     */
    private StatusMessage statusMessage;

    /**
     *
     */
    private String businessName;

    private static String notNull(StatusMessage statusMessage) {
        if (null == statusMessage) {
            throw new IllegalArgumentException("statusMessage can't be null");
        }
        return statusMessage.getMessage();
    }

    public PlatformException() {
        super();
    }

    public PlatformException(String error) {
        super(error);
    }

    public PlatformException(Throwable e) {
        super(e);
    }

    public PlatformException(StatusMessage statusMessage, String businessNam) {
        this(statusMessage, businessNam, null);
    }

    public PlatformException(StatusMessage statusMessage, String businessName, Throwable cause) {
        super(StringUtils.isEmpty(businessName) ? notNull(statusMessage) : businessName + notNull(statusMessage), cause);
        this.statusMessage = statusMessage;
        this.businessName = businessName;
    }

    public String getStatusMessage() {
        return businessName + ":" + statusMessage.getMessage();
    }

    public int getStatusCode() {
        return statusMessage.getCode();
    }

    public StatusMessage statusMessage() {
        return statusMessage;
    }
}
