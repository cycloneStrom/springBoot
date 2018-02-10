package org.poem.community.dao.classify;

import org.poem.core.dao.BaseDTO;
import org.poem.community.entity.PlatformSysClassify;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by poem on 2016/5/6.
 * 基础类的查询
 */

public interface PlatformSysClassDao extends BaseDTO<PlatformSysClassify,String> {

    /**
     * 根据ID 和名称查询出相同名称的记录
     * @param name 名称
     * @param id id
     * @return 查询记录
     */
    @Query("SELECT classify FROM PlatformSysClassify AS classify WHERE  classify.name = ?1 AND  classify.id <> ?2 AND classify.isDel = false ORDER BY  classify.createDate")
    public List<PlatformSysClassify> findClassifyNameByNameAndId(String name,String id);
}
