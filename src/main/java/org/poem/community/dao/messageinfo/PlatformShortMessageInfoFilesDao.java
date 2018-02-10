package org.poem.community.dao.messageinfo;

import org.poem.community.entity.PlatformMessageInfoFiles;
import org.poem.core.dao.BaseDTO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by poem on 2016/5/28.
 * 查询当前的文件的文件信息
 */
public interface PlatformShortMessageInfoFilesDao extends BaseDTO<PlatformMessageInfoFiles, String> {


    /**
     * 根据消息插叙差距信息
     * @param msgId
     * @return
     */
    @Query("SELECT  FILE FROM  PlatformMessageInfoFiles AS  FILE WHERE FILE.platformShortMessageInfo.id = ?1")
    public List<PlatformMessageInfoFiles> queryFileByMsgId(String msgId);
}
