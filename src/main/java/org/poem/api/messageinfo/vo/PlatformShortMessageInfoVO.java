package org.poem.api.messageinfo.vo;

import org.poem.api.user.vo.PlatformSysUserVO;
import org.poem.core.vo.BaseVO;

import java.util.Date;
import java.util.List;

/**
 * Created by poem on 2016/5/16.
 * 说说VO
 */
public class PlatformShortMessageInfoVO extends BaseVO {


    /**
     * 消息体
     */
    private String messageInfo;

    /**
     * 发布时间
     */
    private String publishDate;

    /**
     * 创建人
     */
    private PlatformSysUserVO platformSysUser;

    /**
     * 发布的工具
     */

    public String publishTool;

    /**
     * 浏览次数
     */

    private Long browseCount;

    /**
     * 此条消息赞的次数
     */

    private Long enjoyCount;

    /**
     * 此条记录评论次数
     */
    private Long commentCount;

    /**
     * 收藏次数
     */
    public Long favorityCount;
    /**
     * 上传的文件信息
     */
    private List<PlatformShortMsgFileInfoVO> fileId;


    public Long getFavorityCount() {
        return favorityCount;
    }

    public void setFavorityCount(Long favorityCount) {
        this.favorityCount = favorityCount;
    }

    public List<PlatformShortMsgFileInfoVO> getFileId() {
        return fileId;
    }

    public void setFileId(List<PlatformShortMsgFileInfoVO> fileId) {
        this.fileId = fileId;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public PlatformSysUserVO getPlatformSysUser() {
        return platformSysUser;
    }

    public void setPlatformSysUser(PlatformSysUserVO platformSysUser) {
        this.platformSysUser = platformSysUser;
    }

    public String getPublishTool() {
        return super.getAgentType();
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

    public String getMessageInfo() {

        return messageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }
}
