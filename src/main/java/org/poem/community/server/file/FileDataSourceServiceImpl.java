package org.poem.community.server.file;

import org.apache.commons.lang3.StringUtils;
import org.poem.api.exception.PlatformException;
import org.poem.api.file.FileDataSourceService;
import org.poem.api.file.vo.FileDataSourceVO;
import org.poem.community.dao.file.FileDataSourceDao;
import org.poem.community.entity.FileDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yineng on 2016/12/19.
 * 文件的基本信息的管理
 */
@Service("fileDataSourceService")
@Transactional(rollbackFor = {Exception.class,RuntimeException.class})
public class FileDataSourceServiceImpl  implements FileDataSourceService {

    /**
     * 文件记录信息
     */
    private static Logger logger = LoggerFactory.getLogger( FileDataSourceServiceImpl.class );
    /**
     * 文件信息
     */
    @Autowired
    private FileDataSourceDao fileDataSourceDao;
    /**
     * 查询文件的信息
     * @param fastdfsId
     * @return
     * @throws PlatformException
     */
    @Override
    public FileDataSourceVO getFileInfoById(String fastdfsId) throws PlatformException {
        FileDataSourceVO fileDataSourceVO = null;
        if (StringUtils.isEmpty( fastdfsId )){
            throw  new PlatformException( "FileDataSourceServiceImpl - getFileInfoById : query file info , fastdfsId is not null!" );
        }
        FileDataSource fileDataSource = fileDataSourceDao.queryBySourceId( fastdfsId );
        if(null == fileDataSource){
            return  null;
        }
        fileDataSourceVO = new FileDataSourceVO();
        fileDataSourceVO.setId( fileDataSource.getId() );
        fileDataSourceVO.setFileName( fileDataSource.getFileName() );
        fileDataSourceVO.setFileSize( null != fileDataSource.getFileSize() ? String.valueOf( fileDataSource.getFileSize() ) : null );
        fileDataSourceVO.setBasePath( fileDataSource.getBasePath() );
        fileDataSourceVO.setRelativePath( fileDataSource.getRelativePath() );
        fileDataSourceVO.setGoupName( fileDataSource.getGroupName() );
        fileDataSourceVO.setFastdfsId( fileDataSource.getFastdfsId() );
        return fileDataSourceVO;
    }

    /**
     * 保存文件的信息
     * @param fileDataSourceVO 文件信息vo
     * @throws PlatformException
     */
    @Override
    public void saveFileInfo(FileDataSourceVO fileDataSourceVO) throws PlatformException {
        FileDataSource fileDataSource = this.fileDataSourceDao.queryBySourceId( fileDataSourceVO.getFastdfsId() );
        if(null == fileDataSource){
            fileDataSource = new FileDataSource();
        }
        fileDataSource.setFastdfsId( fileDataSourceVO.getFastdfsId() );
        fileDataSource.setBasePath( fileDataSourceVO.getBasePath() );
        fileDataSource.setRelativePath( fileDataSourceVO.getRelativePath() );
        fileDataSource.setFileName( fileDataSourceVO.getFileName() );
        fileDataSource.setFileSize( StringUtils.isNoneBlank( fileDataSourceVO.getFileSize() ) ? Long.valueOf( fileDataSourceVO.getFileSize() ): null );
        fileDataSource.setResource( fileDataSourceVO.getResource() );
        fileDataSource.setGroupName( fileDataSourceVO.getGoupName());
        this.fileDataSourceDao.save( fileDataSource );
    }

    /**
     * 删除文件
     * @param fastdfsId
     * @throws PlatformException
     */
    @Override
    public FileDataSourceVO deleteFile(String fastdfsId) throws PlatformException {
        FileDataSource fileDataSource = fileDataSourceDao.queryBySourceId( fastdfsId );
        FileDataSourceVO fileDataSourceVO =null;
        if(null != fileDataSource){
            fileDataSourceVO = new FileDataSourceVO();
            fileDataSourceVO.setId( fileDataSource.getId() );
            fileDataSourceVO.setFileName( fileDataSource.getFileName() );
            fileDataSourceVO.setFileSize( null != fileDataSource.getFileSize() ? String.valueOf( fileDataSource.getFileSize() ) : null );
            fileDataSourceVO.setBasePath( fileDataSource.getBasePath() );
            fileDataSourceVO.setRelativePath( fileDataSource.getRelativePath() );
            fileDataSourceVO.setGoupName( fileDataSource.getGroupName() );
            fileDataSourceVO.setFastdfsId( fileDataSource.getFastdfsId() );
            this.fileDataSourceDao.delete( fileDataSource );
        }
        return fileDataSourceVO;
    }
}
