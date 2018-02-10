package org.poem.community.entity;

import org.poem.core.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by yineng on 2016/12/19.
 * 文件记录
 */
@Entity
@Table(name = "FILE_DATA_SOURCE")
public class FileDataSource extends IdEntity {

    /**
     * 文件的名称
     */
    @Column(name = "FILE_NAME")
    private String fileName;

    /**
     * 文件大小
     */
    @Column(name = "FILE_SIZE")
    private Long fileSize;
    /**
     * 文件的原始路径
     */
    @Column(name = "BASE_PATH")
    private String basePath;

    /**
     * 相对路径
     */
    @Column(name = "RELATIVE_PATH")
    private String relativePath;

    /**
     * 文件所在的分组信息
     */
    @Column(name = "GROUP_NAME")
    private String groupName;

    /**
     * 文件在文件服务器上的ip
     */
    @Column(name = "FASTDFS_ID")
    private String fastdfsId;

    /**
     * 文件来源
     * 保存上传的文件的controller文件的名称
     */
    @Column(name = "RESOURCE")
    private String resource;

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }


    public String getFastdfsId() {
        return fastdfsId;
    }

    public void setFastdfsId(String fastdfsId) {
        this.fastdfsId = fastdfsId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
