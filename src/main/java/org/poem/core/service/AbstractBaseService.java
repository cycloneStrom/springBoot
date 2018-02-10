package org.poem.core.service;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.poem.core.dao.BaseDTO;
import org.poem.core.entity.BaseId;
import org.poem.core.jpa.JpaQueryFilter;
import org.poem.core.page.CustomPageImpl;
import org.poem.core.utils.BeanUtils;
import org.poem.core.utils.ReflectionUtils;
import org.poem.core.vo.BaseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created by poem on 2016/4/15.
 * 抽象类，Server层基础服务
 */
public abstract class AbstractBaseService <E extends Serializable, T extends BaseId<E>, K extends BaseVO> implements IInternalService<E, T> {

    @Autowired
    protected EntityManager entityManager;
    /**
     * 数据库实体bean
     */
    protected Class<T> entityClass;
    /**
     * 服务层实体bean
     */
    protected Class<K> voEntityClass;

    @SuppressWarnings("unchecked")
    public AbstractBaseService() {
        this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass(), 0);
        this.voEntityClass = ReflectionUtils.getSuperClassGenricType(getClass(), 1);
    }

    protected abstract BaseDTO<T, String> getDao();

    /**
     * @Title: convert
     * @Description: vo转换为实体
     * @param k
     * @return
     */
    protected T convert(K k) {
        return BeanUtils.copyProperties(entityClass, k);
    }

    /**
     * @Title: convert
     * @Description: 实体转换为vo
     * @param t
     * @return
     */
    protected K convert(T t) {
        return BeanUtils.copyProperties(voEntityClass, t);
    }

    /**
     * @Title: convertToVo
     * @Description: 实体转换为vo
     * @param tList
     * @return
     */
    protected List<K> convertToVo(List<T> tList) {
        List<K> list = new ArrayList<K>();
        if (!CollectionUtils.isEmpty(tList)) {
            K k = null;
            for (T t : tList) {
                k = this.convert(t);
                list.add(k);
            }
        }
        return list;
    }

    /**
     * @Title: convert
     * @Description: vo转换为实体
     * @param kList
     * @return
     */
    protected List<T> convert(List<K> kList) {
        List<T> list = new ArrayList<T>();
        if (!CollectionUtils.isEmpty(kList)) {
            T t = null;
            for (K k : kList) {
                t = this.convert(k);
                list.add(t);
            }
        }
        return list;
    }

    /**
     * @Title: convert
     * @Description: 分页转换
     * @param pageable
     * @param page
     * @return
     */
    protected CustomPageImpl<K> convert(Pageable pageable, Page<T> page) {
        List<K> kList = convertToVo(page.getContent());
        return new CustomPageImpl<K>(kList, pageable, page.getTotalElements());
    }

    /**
     * @Title: getAll
     * @Description: 查询所有
     * @return
     */
    @Override
    public List<T> getAll() {
        return (List<T>) getDao().findAll();
    }

    /**
     * @Title: getVoAll
     * @Description: 查询所有
     * @return
     */

    public List<K> getVoAll() {
        return this.convertToVo((List<T>) getDao().findAll());
    }

    /**
     * @Title: getById
     * @Description: 根据id获取对象信息
     * @param id
     * @return
     */
    @Override
    public T findById(String id) {
        return getDao().findOne(id);
    }

    /**
     * @Title: findVoById
     * @Description: 根据id获取对象信息
     * @param id
     * @return
     */
    public K findVoById(String id) {
        return this.convert(getDao().findOne(id));
    }

    /**
     * @Title: findByPropertysList
     * @Description: 根据条件查询(不去重)
     * @param filters 查询条件
     * @return
     */
    @Override
    public List<T> findByPropertysList(JpaQueryFilter... filters) {
        return findByPropertysList(false, Arrays.asList(filters));
    }

    /**
     * @Title: findByPropertysList
     * @Description: 根据条件查询(不去重)
     * @param filters 查询条件
     * @return
     */
    @Override
    public List<T> findByPropertysList(List<JpaQueryFilter> filters) {
        return findByPropertysList(false, filters);
    }

    /**
     * @Title: findVoByPropertysList
     * @Description: 根据条件查询(不去重)
     * @param filters 查询条件
     * @return
     */
    public List<K> findVoByPropertysList(JpaQueryFilter... filters) {
        return findVoByPropertysList(false, Arrays.asList(filters));
    }

    /**
     * @Title: findVoByPropertysList
     * @Description: 根据条件查询(不去重)
     * @param filters 查询条件
     * @return
     */
    public List<K> findVoByPropertysList(List<JpaQueryFilter> filters) {
        return findVoByPropertysList(false, filters);
    }

    /**
     * @Title: findByPropertysList
     * @Description: 根据条件查询
     * @param isDistinct 是否去重
     * @param filters 查询条件
     * @return
     */
    @Override
    public List<T> findByPropertysList(boolean isDistinct, JpaQueryFilter... filters) {
        return findByPropertysList(isDistinct, Arrays.asList(filters));
    }

    /**
     * @Title: findByPropertysList
     * @Description: 根据条件查询
     * @param isDistinct 是否去重
     * @param filters 查询条件
     * @return
     */
    @Override
    public List<T> findByPropertysList(boolean isDistinct, List<JpaQueryFilter> filters) {
        CriteriaQuery<T> criteriaQuery = getCriteriaQuery(isDistinct, filters);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * @Title: findVoByPropertysList
     * @Description: 根据条件查询
     * @param isDistinct 是否去重
     * @param filters 查询条件
     * @return
     */
    public List<K> findVoByPropertysList(boolean isDistinct, JpaQueryFilter... filters) {
        return findVoByPropertysList(isDistinct, Arrays.asList(filters));
    }

    /**
     * @Title: findVoByPropertysList
     * @Description: 根据条件查询
     * @param isDistinct 是否去重
     * @param filters 查询条件
     * @return
     */
    public List<K> findVoByPropertysList(boolean isDistinct, List<JpaQueryFilter> filters) {
        CriteriaQuery<T> criteriaQuery = getCriteriaQuery(isDistinct, filters);
        List<T> resultList = entityManager.createQuery(criteriaQuery).getResultList();
        return this.convertToVo(resultList);
    }

    /**
     * @Title: findByPropertysPage
     * @Description: 分页查询(不去重)
     * @param filters 查询条件
     * @return
     */
    @Override
    public CustomPageImpl<T> findByPropertysPage(Pageable pageable, JpaQueryFilter... filters) {
        return findByPropertysPage(false, pageable, Arrays.asList(filters));
    }

    /**
     * @Title: findByPropertysPage
     * @Description: 分页查询(不去重)
     * @param filters 查询条件
     * @return
     */
    @Override
    public CustomPageImpl<T> findByPropertysPage(Pageable pageable, List<JpaQueryFilter> filters) {
        return findByPropertysPage(false, pageable, filters);
    }

    /**
     * @Title: findByPropertysPage
     * @Description: 分页查询(不去重)
     * @param filters 查询条件
     * @return
     */
    public CustomPageImpl<K> findVoByPropertysPage(Pageable pageable, JpaQueryFilter... filters) {
        return findVoByPropertysPage(false, pageable, Arrays.asList(filters));
    }

    /**
     * @Title: findByPropertysPage
     * @Description: 分页查询(不去重)
     * @param filters 查询条件
     * @return
     */
    public CustomPageImpl<K> findVoByPropertysPage(Pageable pageable, List<JpaQueryFilter> filters) {
        return findVoByPropertysPage(false, pageable, filters);
    }

    /**
     * @Title: findByPropertysPage
     * @Description: 分页查询
     * @param isDistinct 是否去重
     * @param filters 查询条件
     * @return
     */
    @Override
    public CustomPageImpl<T> findByPropertysPage(final boolean isDistinct, Pageable pageable, final JpaQueryFilter... filters) {
        return findByPropertysPage(isDistinct, pageable, Arrays.asList(filters));
    }

    /**
     * @Title: findByPropertysPage
     * @Description: 分页查询
     * @param isDistinct 是否去重
     * @param filters 查询条件
     * @return
     */
    @Override
    public CustomPageImpl<T> findByPropertysPage(final boolean isDistinct, Pageable pageable, final List<JpaQueryFilter> filters) {
        Page<T> page = getDao().findAll(new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                buildCriteriaQueryByFilters(root, criteriaBuilder, criteriaQuery, filters);
                return null;
            }
        }, pageable);
        return new CustomPageImpl<T>(page.getContent(), pageable, page.getTotalElements());
    }

    /**
     * @Title: findVoByPropertysPage
     * @Description: 分页查询
     * @param isDistinct 是否去重
     * @param filters 查询条件
     * @return
     */
    public CustomPageImpl<K> findVoByPropertysPage(final boolean isDistinct, Pageable pageable, final JpaQueryFilter... filters) {
        return findVoByPropertysPage(pageable, Arrays.asList(filters));
    }

    /**
     * @Title: findVoByPropertysPage
     * @Description: 分页查询
     * @param isDistinct 是否去重
     * @param filters 查询条件
     * @return
     */
    public CustomPageImpl<K> findVoByPropertysPage(final boolean isDistinct, Pageable pageable, final List<JpaQueryFilter> filters) {
        Page<T> page = this.findByPropertysPage(isDistinct, pageable, filters);
        return convert(pageable, page);
    }

    /**
     * @Title: findByPropertysPage
     * @Description: 根据map参数分页
     * @param searchParams 查询条件
     * @return
     */
    @Override
    public CustomPageImpl<T> findByPropertysPage(Pageable pageable, Map<String, Object> searchParams) {
        Specification<T> spec = this.buildSpecification(searchParams);
        Page<T> page = getDao().findAll(spec, pageable);
        return new CustomPageImpl<T>(page.getContent(), pageable, page.getTotalElements());
    }

    /**
     * @Title: findVoByPropertysPage
     * @Description: 根据map参数分页
     * @param searchParams 查询条件
     * @return
     */
    public CustomPageImpl<K> findVoByPropertysPage(Pageable pageable, Map<String, Object> searchParams) {
        Specification<T> spec = this.buildSpecification(searchParams);
        Page<T> page = getDao().findAll(spec, pageable);
        return convert(pageable, page);
    }

    /**
     * @Title: save
     * @Description: 保存对象
     * @param k
     */
    public void saveVo(K k) {
        this.save(this.convert(k));
    }

    /**
     * @Title: saveVo
     * @Description: 保存对象
     * @param ks
     */
    public void saveVo(List<K> ks) {
        this.save(this.convert(ks));
    }

    /**
     * @Title: save
     * @Description: 保存对象
     * @param t
     */
    @Override
    public void save(T t) {
        getDao().save(t);
    }

    /**
     * @Title: save
     * @Description: 保存对象
     * @param ts
     */
    @Override
    public void save(List<T> ts) {
        getDao().save(ts);
    }

    /**
     * @Title: del
     * @Description: 根据主键删除对象
     * @param id
     */
    @Override
    public void del(String id) {
        getDao().delete(id);
    }

    /**
     * @Title: del
     * @Description: 根据对象删除数据
     * @param k
     */
    public void delVo(K k) {
        getDao().delete(this.convert(k));
    }
    /**
     * @Title: delAll
     * @Description: 批量删除(vo)
     * @param list
     */
    public void delVoAll(List<K> list) {
        for (K k : list) {
            this.delVo(k);
        }
    }

    /**
     * @Title: delAll
     * @Description: 批量删除
     * @param ids
     */
    @Override
    public void delAll(String... ids) {
        for (String id : ids) {
            this.del(id);
        }
    }

    @Override
    public void delAll(List<T> list) {
        getDao().delete(list);
    }



    /**
     * @Title: getCriteriaQuery
     * @Description: 封装query(不去重)
     * @param filters 查询条件
     * @return
     */
    protected CriteriaQuery<T> getCriteriaQuery(JpaQueryFilter... filters) {
        return getCriteriaQuery(Arrays.asList(filters));
    }

    /**
     * @Title: getCriteriaQuery
     * @Description: 封装query(不去重)
     * @param filters 查询条件
     * @return
     */
    protected CriteriaQuery<T> getCriteriaQuery(List<JpaQueryFilter> filters) {
        return getCriteriaQuery(false);
    }

    /**
     * 根据filters构建CriteriaQuery
     *
     * @param root Query roots always reference entities.
     * @param criteriaBuilder Used to construct criteria queries, compound selections,expressions, predicates, orderings.
     * @param criteriaQuery 构建出来的查询对象
     * @param filters 查询条件
     */
    protected void bulidCriteriaQueryByFilters(Root<T> root, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery, JpaQueryFilter... filters) {
        this.buildCriteriaQueryByFilters(root, criteriaBuilder, criteriaQuery, Arrays.asList(filters));
    }

    /**
     * 根据filters构建CriteriaQuery
     * //to do 新添加的sub在此扩展
     * @param root Query roots always reference entities.
     * @param criteriaBuilder Used to construct criteria queries, compound selections,expressions, predicates, orderings.
     * @param criteriaQuery 构建出来的查询对象
     * @param filters 查询条件
     */
    protected List<Predicate> buildCriteriaQueryByFilters(Root<T> root, CriteriaBuilder criteriaBuilder, CriteriaQuery<?> criteriaQuery, List<JpaQueryFilter> filters) {
        List<Predicate> predicateList = new ArrayList<Predicate>();
        List<Order> orderList = new ArrayList<Order>();
        List<Expression<?>> groupList = new ArrayList<Expression<?>>();
        Predicate predicate = null;
        Expression<?> expression = null;
        Object value = null;
        for (JpaQueryFilter filter : filters) {
            expression = buildExpression(root, filter.getProperty());
            value = filter.getValue();
            // 排序
            orderBy(criteriaBuilder, orderList, expression, filter);
            //如果参数值为空，则不进行条件拼装 filter.getValue() == null  && !filter.isOrderBy()
            if (StringUtils.isBlank(filter.getProperty()) || value == null ) {
                continue;
            }
            joinWith(root, filter);
            if(filter.getJpaQueryFilterList()!=null&&filter.getJpaQueryFilterList().size()>1){
                List<Predicate> subPredicateList=buildSubCriteriaQueryByFilters(root, criteriaBuilder, filter.getJpaQueryFilterList());
                Predicate subPredicate=null;
                Predicate[] subPredicates=new Predicate[subPredicateList.size()];
                if(subPredicateList.size()>1){
                    if(JpaQueryFilter.JpaQueryFilterTypeEnum.OR.equals(filter.getFilterTypeEnum())){
                        subPredicate= criteriaBuilder.or(subPredicateList.toArray(subPredicates));
                    }else if(JpaQueryFilter.JpaQueryFilterTypeEnum.AND.equals(filter.getFilterTypeEnum())){
                        subPredicate=criteriaBuilder.and(subPredicateList.toArray(subPredicates));
                    }
                    if(subPredicate!=null){
                        predicateList.add(subPredicate);
                    }
                }
            }
            // to do sub在此扩展

            // 条件
            predicate = condition(criteriaBuilder, expression, value, filter);
            // 分组
            if (filter.isGroup()) {
                groupList.add(expression);
            }

            // 拼接类型
            if (JpaQueryFilter.JpaQueryFilterTypeEnum.OR.equals(filter.getFilterTypeEnum())) {
                predicateList.add(criteriaBuilder.or(predicate));
            } else {
                predicateList.add(criteriaBuilder.and(predicate));
            }

        }
        if (!CollectionUtils.isEmpty(orderList)) {// 添加排序
            criteriaQuery.orderBy(orderList);
        } else {
            Expression<?> orderExp = buildExpression(root, "id");
            if (orderExp != null) {
                criteriaQuery.orderBy(criteriaBuilder.desc(orderExp));
            }
        }
        if (!CollectionUtils.isEmpty(predicateList)) {// 添加查询条件
            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
        }

        if (!CollectionUtils.isEmpty(groupList)) {// 添加分组
            criteriaQuery.groupBy(groupList);
        }
        return predicateList;
    }

    protected List<Predicate> buildSubCriteriaQueryByFilters(Root<T> root, CriteriaBuilder criteriaBuilder, List<JpaQueryFilter> filters) {
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate predicate = null;
        Expression<?> expression = null;
        Object value = null;
        for (JpaQueryFilter filter : filters) {
            expression = buildExpression(root, filter.getProperty());
            value = filter.getValue();
            if (StringUtils.isBlank(filter.getProperty()) || value == null ) {
                continue;
            }
            predicate = condition(criteriaBuilder, expression, value, filter);
            predicateList.add(predicate);
        }
        return predicateList;
    }

    private Predicate condition(CriteriaBuilder criteriaBuilder, Expression<?> expression, Object value, JpaQueryFilter filter) {
        Predicate predicate = null;
        switch (filter.getFilterEnum()) {
            case NOT_EQ:
                predicate = criteriaBuilder.notEqual(expression, value);
                break;
            case IS_NULL:
                predicate = criteriaBuilder.isNull(expression);
                break;
            case IS_NOT_NULL:
                predicate = criteriaBuilder.isNotNull(expression);
                break;
            case LT:
                predicate = criteriaBuilder.lt(expression.as(Number.class), (Number) value);
                break;
            case GT:
                predicate = criteriaBuilder.gt(expression.as(Number.class), (Number) value);
                break;
            case LE:
                predicate = criteriaBuilder.le(expression.as(Number.class), (Number) value);
                break;
            case GE:
                predicate = criteriaBuilder.ge(expression.as(Number.class), (Number) value);
                break;
            case BETWEEN:
                predicate = getPredicateByBetween(criteriaBuilder, expression, (Object[]) value);
                break;
            case LIKE:
                predicate = getPredicateByLike(criteriaBuilder, expression, (String) value);
                break;
            case NOT_LIKE:
                predicate = criteriaBuilder.not(getPredicateByLike(criteriaBuilder, expression, (String) value));
                break;
            case IN:
                predicate = getPredicateByIn(criteriaBuilder, expression, value);
                break;
            case NOT_IN:
                predicate = criteriaBuilder.not(getPredicateByIn(criteriaBuilder, expression, value));
                break;
            default:// 默认EQ
                predicate = criteriaBuilder.equal(expression, value);
                break;
        }
        return predicate;
    }

    private void joinWith(Root<T> root, JpaQueryFilter filter) {
        switch (filter.getFilterJoinEnum()) {
            case LEFT:
                root.join(filter.getProperty(), JoinType.LEFT);
                break;
            case INNER:
                root.join(filter.getProperty(), JoinType.INNER);
                break;
            case RIGHT:
                root.join(filter.getProperty(), JoinType.RIGHT);
                break;
            default:
        }
    }

    private void orderBy(CriteriaBuilder criteriaBuilder, List<Order> orderList, Expression<?> expression, JpaQueryFilter filter) {
        Order order;
        if (filter.isOrderBy()) {
            if (JpaQueryFilter.JpaQueryFilterOrderEnum.DESC.equals(filter.getFilterOrderEnum())) {
                order = criteriaBuilder.desc(expression);
            } else {
                order = criteriaBuilder.asc(expression);
            }
            orderList.add(order);
        }
    }

    /**
     * @Title: getCriteriaQuery
     * @Description: 封装query
     * @param isDistinct 是否去重
     * @param filters 查询条件
     * @return
     */
    protected CriteriaQuery<T> getCriteriaQuery(boolean isDistinct, JpaQueryFilter... filters) {
        return getCriteriaQuery(isDistinct, Arrays.asList(filters));
    }

    /**
     * @Title: getCriteriaQuery
     * @Description: 封装query
     * @param isDistinct 是否去重
     * @param filters 查询条件
     * @return
     */
    protected CriteriaQuery<T> getCriteriaQuery(boolean isDistinct, List<JpaQueryFilter> filters) {
        CriteriaBuilder criteriaBuilder = this.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = this.getCriteriaQuery(criteriaBuilder);
        Root<T> root = this.getRoot(criteriaQuery);
        buildCriteriaQueryByFilters(root, criteriaBuilder, criteriaQuery, filters);
        // 去重
        if (isDistinct) {
            criteriaQuery.distinct(true);
        }
        return criteriaQuery;
    }

    /**
     * @Title: buildExpression
     * @Description: 构建外链
     * @param root
     * @param filterProperty
     * @return
     */
    private Expression<T> buildExpression(Root<T> root, String filterProperty) {
        Path<T> path = null;
        for (String property : filterProperty.split("\\.")) {
            if (path == null) {
                path = root.get(property);
            } else {
                path = path.get(property);
            }
        }
        return path;
    }

    /**
     * @Title: getPredicateByLike
     * @Description: 获取like条件
     * @param criteriaBuilder
     * @param expression
     * @param pValue
     * @return
     */
    private Predicate getPredicateByLike(CriteriaBuilder criteriaBuilder, Expression<?> expression, String pValue) {
        String value = pValue;
        if (StringUtils.indexOf(pValue, "%") < 0) {
            value = "%" + pValue + "%";
        }
        return criteriaBuilder.like(expression.as(String.class), value);
    }

    /**
     * @Title: getPredicateByIn
     * @Description: 获取in条件
     * @param criteriaBuilder
     * @param expression
     * @param value
     * @return
     */
    private Predicate getPredicateByIn(CriteriaBuilder criteriaBuilder, Expression<?> expression, Object value) {
        CriteriaBuilder.In<Object> in = criteriaBuilder.in(expression);
        List<Object> list = Lists.newArrayList(value);
        for (Object obj : list) {
            in.value(obj);
        }
        return in;
    }

    /**
     * @Title: getPredicateByBetween
     * @Description: 获取between条件
     * @param criteriaBuilder
     * @param expression
     * @param values
     * @return
     */
    private Predicate getPredicateByBetween(CriteriaBuilder criteriaBuilder, Expression<?> expression, Object... values) {
        Predicate predicate = null;
        if (values[0] instanceof Date) {
            predicate = criteriaBuilder.between(expression.as(Date.class), (Date) values[0], (Date) values[1]);
        } else if (values[0] instanceof Number) {
            Predicate predicateLe = criteriaBuilder.le(expression.as(Number.class), (Number) values[1]);
            Predicate predicateGe = criteriaBuilder.ge(expression.as(Number.class), (Number) values[0]);
            predicate = criteriaBuilder.and(predicateLe, predicateGe);
        }
        return predicate;
    }

    /**
     * @Title: buildSpecification
     * @Description: 构建查询条件
     * @param searchParams
     * @return
     */
    protected Specification<T> buildSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        return  DynamicSpecifications.bySearchFilter(filters.values(), entityClass);
    }

    protected CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    protected CriteriaQuery<T> getCriteriaQuery(CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.createQuery(entityClass);
    }

    protected Root<T> getRoot(CriteriaQuery<T> criteriaQuery) {
        return criteriaQuery.from(entityClass);
    }
}
