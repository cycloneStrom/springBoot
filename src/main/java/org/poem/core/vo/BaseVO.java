package org.poem.core.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by poem on 2016/4/15.
 * 基础vo 序列化
 */
public class BaseVO implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5771973207268837024L;
    /**
     * 主键
     */
    private String id;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 排序列集合
     */
    private Map<String, String> sortFieldMap;

    /**
     * 用户使用的ip
     */
    private String ipAddress;

    /**
     * 用户使用的类型
     */
    private String agentType;

    /**
     * 浏览器类型
     */
    private String browserMsg;

    public BaseVO() {
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public BaseVO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getSortFieldMap() {
        return sortFieldMap;
    }

    public void setSortFieldMap(Map<String, String> sortFieldMap) {
        this.sortFieldMap = sortFieldMap;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public String getBrowserMsg() {
        return browserMsg;
    }

    public void setBrowserMsg(String browserMsg) {
        this.browserMsg = browserMsg;
    }
}
