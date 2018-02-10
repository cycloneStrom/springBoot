package org.poem.core.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * Created by poem on 2016/6/18.
 *  * 全局DTO  基本数据层
 * Spring@4 Date Jpa
 *
 * JpaSpecificationExecutor Spring jpa
 */
@NoRepositoryBean
public interface BaseRepository<T, I extends Serializable> extends PagingAndSortingRepository<T, I>,JpaSpecificationExecutor<T> {


}