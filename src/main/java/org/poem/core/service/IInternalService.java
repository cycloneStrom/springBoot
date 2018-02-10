package org.poem.core.service;

import org.poem.core.entity.BaseId;
import org.poem.core.page.CustomPageImpl;
import org.poem.core.jpa.JpaQueryFilter;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by poem on 2016/4/15.
 * 基础 服务
 */
public interface IInternalService<U extends Serializable, T extends BaseId<U>> {


    List<T> getAll();

    T findById(String id);

    List<T> findByPropertysList(JpaQueryFilter... filters);

    List<T> findByPropertysList(List<JpaQueryFilter> filters);

    List<T> findByPropertysList(boolean isDistinct, JpaQueryFilter... filters);

    List<T> findByPropertysList(boolean isDistinct, List<JpaQueryFilter> filters);

    CustomPageImpl<T> findByPropertysPage(Pageable pageable, JpaQueryFilter... filters);

    CustomPageImpl<T> findByPropertysPage(Pageable pageable, List<JpaQueryFilter> filters);

    CustomPageImpl<T> findByPropertysPage(boolean isDistinct, Pageable pageable, JpaQueryFilter... filters);

    CustomPageImpl<T> findByPropertysPage(boolean isDistinct, Pageable pageable, List<JpaQueryFilter> filters);

    CustomPageImpl<T> findByPropertysPage(Pageable pageable, Map<String, Object> searchParams);

    void save(T t);

    void save(List<T> ts);

    void del(String id);

    void delAll(String... ids);

    void delAll(List<T> list);

}
