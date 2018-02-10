package org.poem.core.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Created by poem on 2016/4/15.
 * 是否可以删除
 */
// JPA 基类
@MappedSuperclass
public abstract class BaseEntity extends IdEntity {

    /**
     * 是否可以删除
     */
    @Column(name = "IS_DEL")
    private Boolean isDel = false;


    public Boolean getIsDel() {
        return isDel;
    }


    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }
}
