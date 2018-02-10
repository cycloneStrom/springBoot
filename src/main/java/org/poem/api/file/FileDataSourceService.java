package org.poem.api.file;


import org.poem.api.exception.PlatformException;
import org.poem.api.file.vo.FileDataSourceVO;

/**
 * Created by yineng on 2016/12/19.
 * 文件接口
 */
public interface FileDataSourceService {

    /**
     * 根据文件的id获取文件的信息
     * @param fastdfsId
     * @return
     * @throws PlatformException
     */
    public FileDataSourceVO getFileInfoById(String fastdfsId) throws PlatformException;

    /**
     *  保存文件的信息
     * @param fileDataSourceVO 文件信息vo
     * @throws PlatformException
     */
    public void saveFileInfo(FileDataSourceVO fileDataSourceVO) throws PlatformException;

    /**
     * 删除文件
     * @param fastdfsId
     * @throws PlatformException
     */
    public FileDataSourceVO deleteFile(String fastdfsId) throws PlatformException;
}
