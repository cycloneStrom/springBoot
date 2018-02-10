package org.poem.community.dao.file;

import org.poem.community.entity.FileDataSource;
import org.poem.core.dao.BaseDTO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by yineng on 2016/12/19.
 * 文件的信息
 */
public interface FileDataSourceDao extends BaseDTO<FileDataSource,String>{

    /**
     * 根据文件的fastdfsid查询出文件的信息
     * @param fastdfsId
     * @return
     */
    @Query("SELECT source FROM FileDataSource AS source WHERE source.fastdfsId=?1")
    public FileDataSource queryBySourceId(String fastdfsId) ;

    @Query("SELECT source FROM FileDataSource AS source WHERE source.fastdfsId IN (?1)")
    public List<FileDataSource> queryBySourceIds(List<String> fastdfsId) ;
}
