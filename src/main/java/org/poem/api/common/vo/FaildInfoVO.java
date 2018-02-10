package org.poem.api.common.vo;

import org.poem.core.vo.BaseVO;

/**
 * Created by poem on 2016/5/6.
 */
public class FaildInfoVO extends BaseVO{

    /**
     * 失败日期
     */
    private String DateTime;

    /**
     * 失败操作人
     */
    private String userName;

    /**
     * 失败原因
     */
    private String failedReason;

    public FaildInfoVO() {
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFailedReason() {
        return failedReason;
    }

    public void setFailedReason(String failedReason) {
        this.failedReason = failedReason;
    }

    public FaildInfoVO(String id, String failedReason) {
        super(id);
        this.failedReason = failedReason;
    }
}
