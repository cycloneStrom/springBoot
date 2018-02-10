package org.poem.community.entity;

import org.poem.core.entity.IdEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by poem on 2016/5/16.
 * 消息提
 */
@Entity
@Table(name = "PLATFORM_MESSAGE_INFO")
@Cacheable
public class PlatformMessageInfo extends IdEntity {

    /**
     * 消息体
     */
    @Column(name = "MMESSAGE_INFO", nullable = false, length = 140)
    private String messageInfo;

    /**
     * 发布时间
     */
    @Column(name = "PUBLISH_DATE")
    private Date publishDate;

    /**
     * 创建人
     */
    @ManyToOne
    @JoinColumn(name = "PLATFORM_SYS_USER_ID")
    private PlatformSysUser platformSysUser;

    /**
     * 发布的工具
     */
    @Column(name = "PUBLISH_TOOL")
    private String publishTool;

    /**
     * 浏览次数
     */
    @Column(name = "BROWSE_COUNT")
    private Long browseCount;

    /**
     * 此条消息收藏的次数
     */
    @Column(name = "FAVORITY_COUNT")
    private Long favorityCount;

    /**
     * 此条消息赞的次数
     */
    @Column(name = "ENJOY_COUNT")
    private Long enjoyCount;

    /**
     * 此条记录评论次数
     */
    @Column(name = "COMMENT_COUNT")
    private Long commentCount;


    /**
     * 消息浏览人
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "BROWSE_USER_IDS", columnDefinition = "TEXT")
    private String browseUserIds;

    /**
     * 赞过本条说说的人的id，用英文的逗号隔开
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ENJOY_USER_IDS", columnDefinition = "TEXT")
    private String enjoyUserIds;

    /**
     * 收藏过本条说说的人的ID，多个使用英文的都好隔开
     */
    @Lob
    @Basic(fetch = FetchType.LAZY) //@Basic(fetch = FetchType.LAZY)   提高响应的速度
    @Column(name = "FAVORITY_USER_IDS", columnDefinition = "TEXT")
    private String favorityUserIds;


    /**
     * 消息附件
     */
    @OneToMany(mappedBy = "platformShortMessageInfo", fetch = FetchType.LAZY)
    private List<PlatformMessageInfoFiles> platformMessageInfoFiles ;


    public List<PlatformMessageInfoFiles> getPlatformMessageInfoFiles() {
        return platformMessageInfoFiles;
    }

    public void setPlatformMessageInfoFiles(List<PlatformMessageInfoFiles> platformMessageInfoFiles) {
        this.platformMessageInfoFiles = platformMessageInfoFiles;
    }

    public Long getFavorityCount() {
        return favorityCount;
    }

    public void setFavorityCount(Long favorityCount) {
        this.favorityCount = favorityCount;
    }

    public String getBrowseUserIds() {
        return browseUserIds;
    }

    public void setBrowseUserIds(String browseUserIds) {
        this.browseUserIds = browseUserIds;
    }

    public String getEnjoyUserIds() {
        return enjoyUserIds;
    }

    public void setEnjoyUserIds(String enjoyUserIds) {
        this.enjoyUserIds = enjoyUserIds;
    }

    public String getFavorityUserIds() {
        return favorityUserIds;
    }

    public void setFavorityUserIds(String favorityUserIds) {
        this.favorityUserIds = favorityUserIds;
    }

    public String getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public PlatformSysUser getPlatformSysUser() {
        return platformSysUser;
    }

    public void setPlatformSysUser(PlatformSysUser platformSysUser) {
        this.platformSysUser = platformSysUser;
    }

    public String getPublishTool() {
        return publishTool;
    }

    public void setPublishTool(String publishTool) {
        this.publishTool = publishTool;
    }

    public Long getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(Long browseCount) {
        this.browseCount = browseCount;
    }

    public Long getEnjoyCount() {
        return enjoyCount;
    }

    public void setEnjoyCount(Long enjoyCount) {
        this.enjoyCount = enjoyCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }
}
