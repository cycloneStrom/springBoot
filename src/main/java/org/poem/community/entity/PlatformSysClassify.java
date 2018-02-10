package org.poem.community.entity;

import org.poem.core.entity.BaseEntity;
import org.poem.core.entity.IdEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by poem on 2016/4/28.
 * 基础大类
 */
@Entity
@Table(name="PLATFORM_SYS_CLASSIFY")
@Cacheable
public class PlatformSysClassify extends BaseEntity {

    /**
     * 名称
     */
    @Column(name = "NAME",length = 250)
    private String name;

    /**
     * 描述
     */
    @Column(name = "CLASS_DESCRIBE")
    private String describe;

    /**
     * 创建人
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "PLATFORM_SYS_USER_ID")
    private PlatformSysUser platformSysUser;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;

    /**
     * 人数
     */
    @Column(name = "NUMBER")
    private Long number;

    public Date getCreateDate() {
        return createDate;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 创建人
     */

    public PlatformSysUser getPlatformSysUser() {
        return platformSysUser;
    }

    public void setPlatformSysUser(PlatformSysUser platformSysUser) {
        this.platformSysUser = platformSysUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
