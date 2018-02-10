package org.poem.api.file.vo;

import org.poem.core.vo.BaseVO;

/**
 * Created by yineng on 2016/12/19.
 * 文件的信息
 */
public class FileDataSourceVO  extends BaseVO{
    /**
     * 文件的名称
     */
    private String fileName;

    /**
     *  文件大小
     */
    private String fileSize;
    /**
     * 文件的原始路径
     */
    private String basePath;

    /**
     * 相对路径
     */
    private String relativePath;

    /**
     * 文件所在的分组信息
     */
    private String goupName;

    /**
     * 文件在文件服务器上的ip
     */
    private String fastdfsId;

    /**
     * 文件来源
     * 保存上传的文件的controller文件的名称
     */
    private String resource;


    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
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

    public String getGoupName() {
        return goupName;
    }

    public void setGoupName(String goupName) {
        this.goupName = goupName;
    }

    public String getFastdfsId() {
        return fastdfsId;
    }

    public void setFastdfsId(String fastdfsId) {
        this.fastdfsId = fastdfsId;
    }
}
