package org.poem.core.jpa;

import java.io.Serializable;
import java.util.List;

/**
 * 查询条件封装类
 */
public class JpaQueryFilter implements Serializable {

    public enum JpaQueryFilterEnum {
        EQ, NOT_EQ, IS_NULL, IS_NOT_NULL, LT, GT, GE, LE, BETWEEN, LIKE, NOT_LIKE, IN, NOT_IN;
    }

    public enum JpaQueryFilterTypeEnum {
        AND, OR;
    }

    public enum JpaQueryFilterOrderEnum {
        ASC, DESC;
    }

    public enum JpaQueryFilterJoinEnum {
        NONE,LEFT, INNER, RIGHT;
    }

    private List<JpaQueryFilter> jpaQueryFilterList;

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3493388565595352888L;

    /**
     * 属性
     */
    private String property;

    /**
     * 值(JpaQueryFilterEnum.BETWEEN类型时value值是长度为2的数组)
     */
    private Object value;

    /**
     * 是否排序
     */
    private boolean isOrderBy = false;

    /**
     * 是否分组
     */
    private boolean isGroup = false;

    /**
     * 排序类型
     */
    private JpaQueryFilterOrderEnum filterOrderEnum = JpaQueryFilterOrderEnum.ASC;

    /**
     * 条件类型
     */
    private JpaQueryFilterEnum filterEnum = JpaQueryFilterEnum.EQ;

    /**
     * 条件拼接类型
     */
    private JpaQueryFilterTypeEnum filterTypeEnum = JpaQueryFilterTypeEnum.AND;
    private JpaQueryFilterJoinEnum filterJoinEnum = JpaQueryFilterJoinEnum.NONE;

    public JpaQueryFilter(String property, Object value) {
        this.property = property;
        this.value = value;
    }

    public JpaQueryFilter(String property, Object value, boolean isOrderBy) {
        this.property = property;
        this.value = value;
        this.isOrderBy = isOrderBy;
    }

    public JpaQueryFilter(String property, Object value, boolean isOrderBy, JpaQueryFilterOrderEnum filterOrderEnum) {
        this.property = property;
        this.value = value;
        this.isOrderBy = isOrderBy;
        this.filterOrderEnum = filterOrderEnum;
    }

    public JpaQueryFilter(String property, Object value, JpaQueryFilterEnum filterEnum) {
        this.property = property;
        this.value = value;
        this.filterEnum = filterEnum;
    }

    public JpaQueryFilter(String property, Object value, JpaQueryFilterEnum filterEnum, JpaQueryFilterTypeEnum filterTypeEnum) {
        this.property = property;
        this.value = value;
        this.filterEnum = filterEnum;
        this.filterTypeEnum = filterTypeEnum;
    }

    public JpaQueryFilter(String property, Object value, JpaQueryFilterEnum filterEnum, JpaQueryFilterTypeEnum filterTypeEnum, JpaQueryFilterJoinEnum filterJoinEnum) {
        this.property = property;
        this.value = value;
        this.filterEnum = filterEnum;
        this.filterTypeEnum = filterTypeEnum;
        this.filterJoinEnum = filterJoinEnum;
    }

    public JpaQueryFilter(List<JpaQueryFilter> filterList,JpaQueryFilterTypeEnum filterTypeEnum){
        this.jpaQueryFilterList=filterList;
        this.filterTypeEnum=filterTypeEnum;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isOrderBy() {
        return isOrderBy;
    }

    public void setOrderBy(boolean isOrderBy) {
        this.isOrderBy = isOrderBy;
    }

    public void setOrderBy(boolean isOrderBy, JpaQueryFilterOrderEnum filterOrderEnum) {
        this.isOrderBy = isOrderBy;
        this.filterOrderEnum = filterOrderEnum;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    public JpaQueryFilterOrderEnum getFilterOrderEnum() {
        return filterOrderEnum;
    }

    public void setFilterOrderEnum(JpaQueryFilterOrderEnum filterOrderEnum) {
        this.filterOrderEnum = filterOrderEnum;
    }

    public JpaQueryFilterEnum getFilterEnum() {
        return filterEnum;
    }

    public void setFilterEnum(JpaQueryFilterEnum filterEnum) {
        this.filterEnum = filterEnum;
    }

    public JpaQueryFilterTypeEnum getFilterTypeEnum() {
        return filterTypeEnum;
    }

    public void setFilterTypeEnum(JpaQueryFilterTypeEnum filterTypeEnum) {
        this.filterTypeEnum = filterTypeEnum;
    }

    public JpaQueryFilterJoinEnum getFilterJoinEnum() {
        return filterJoinEnum;
    }

    public void setFilterJoinEnum(JpaQueryFilterJoinEnum filterJoinEnum) {
        this.filterJoinEnum = filterJoinEnum;
    }

    public List<JpaQueryFilter> getJpaQueryFilterList() {
        return jpaQueryFilterList;
    }

    public void setJpaQueryFilterList(List<JpaQueryFilter> jpaQueryFilterList) {
        this.jpaQueryFilterList = jpaQueryFilterList;
    }
}