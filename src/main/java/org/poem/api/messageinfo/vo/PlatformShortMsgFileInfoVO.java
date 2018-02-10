package org.poem.api.messageinfo.vo;

import org.poem.core.vo.BaseVO;

/**
 * Created by poem on 2016/5/28.
 * 短消息的实体
 */
public class PlatformShortMsgFileInfoVO extends BaseVO{

    /**
     * 文件的名字
     */
    private String fileName;

    /**
     * 文件的id
     */
    private String fileId;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
