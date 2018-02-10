package org.poem.community.entity;

import org.poem.core.entity.BaseEntity;
import org.poem.core.entity.IdEntity;

import javax.persistence.*;

/**
 * Created by poem on 2016/5/16.
 * 消息提的文件信息
 */
@Entity
@Table(name = "PLATFORM_MESSAGE_INFO_FILES")
@Cacheable
public class PlatformMessageInfoFiles extends IdEntity {

    /**
     * 文件保存的ID
     */
    @Column(name = "FILE_INFO")
    private String fileInfo;

    /**
     * 文件名字
     */
    @Column(name = "FILE_NAME")
    private String fileName;

    /**
     * 消息体
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLATFORM_MESSAGE_INFO_ID")
    private PlatformMessageInfo platformShortMessageInfo;

    public String getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(String fileInfo) {
        this.fileInfo = fileInfo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public PlatformMessageInfo getPlatformShortMessageInfo() {
        return platformShortMessageInfo;
    }

    public void setPlatformShortMessageInfo(PlatformMessageInfo platformShortMessageInfo) {
        this.platformShortMessageInfo = platformShortMessageInfo;
    }
}
