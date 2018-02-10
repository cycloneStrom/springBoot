package org.poem.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by poem on 2016/4/15.
 * 全局DTO  基本数据层
 * Spring@4 Date Jpa
 *
 * JpaSpecificationExecutor Spring jpa
 */
@NoRepositoryBean
public interface BaseDTO<T, I extends Serializable> extends JpaRepository<T, I>,JpaSpecificationExecutor<T> {

}
