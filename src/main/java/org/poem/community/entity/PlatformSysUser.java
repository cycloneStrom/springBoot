package org.poem.community.entity;

import org.poem.core.entity.BaseEntity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by poem on 2016/5/10.
 * 用户表
 */
@Entity
@Cacheable
@Table(name = "PLATFORM_SYS_USER")
public class PlatformSysUser extends BaseEntity {

    /**
     * 实际名字
     */
    @Column(name = "NAME", nullable = true)
    private String name;

    /**
     * 性别 0 女 1 男
     */
    @Column(name="GENDER")
    private Byte gender;

    /**
     * 用户身份证号
     */
    @Column(name = "NUMBER")
    private String number;

    /**
     * 用户的生日
     */
    @Column(name = "DATE_OF_BIRTH")
    private Date birthday;
    /**
     * 用户网名
     */
    @Column(name = "ALIEN")
    private String alien;

    /**
     * 用户邮箱
     */
    @Column(name = "EMAIL")
    private String email;


    /**
     * 用户头像
     */
    @Column(name="HEAD_URL")
    private String headUrl;

    /**
     * 创建时间
     */
    @Column(name="CREATE_DATE_TIME")
    private Date createDateTime;

    /**
     * 修改时间
     */
    @Column(name="MODIFY_DATE_TIME")
    private Date modifyDateTime;


    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Date getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(Date modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAlien() {
        return alien;
    }

    public void setAlien(String alien) {
        this.alien = alien;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
