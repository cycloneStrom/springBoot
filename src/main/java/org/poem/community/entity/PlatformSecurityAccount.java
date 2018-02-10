package org.poem.community.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.poem.common.log.Comment;
import org.poem.common.log.LogIgnore;
import org.poem.core.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by poem on 2016/6/18.
 */
@Entity
@Table(name = "PLATFORM_SECURITY_ACCOUNT")
@Cacheable
@Comment("用户账号登录")
public class PlatformSecurityAccount extends BaseEntity {

    /**
     * account 账号
     */
    @Column(name = "ACCOUNT")
    @Comment("账号")
    private String account;

    /**
     * password 密码
     */
    @Column(name = "PASSWORD")
    @LogIgnore
    private String password;

    /**
     * token TOKEN
     */
    @Column(name = "TOKEN")
    @LogIgnore
    private String token;

    /**
     * isExpired 是否过期 1-是，0-否
     */
    @Column(name = "IS_EXPIRED")
    @LogIgnore
    private Boolean isExpired;

    /**
     * isLock 是否锁定 true-是，false-否
     */
    @Column(name = "IS_LOCK")
    @LogIgnore
    private Boolean isLock;

    /**
     * createTime 创建时间
     */
    @Column(name = "CREATE_TIME")
    @Comment("创建时间")
    private Date createTime;

    /**
     * status 状态：1=激活，0=冻结，-1=注销
     */
    @Column(name = "STATUS")
    @Comment("状态")
    private Byte status;

    /**
     * lastLoginTime 上次登录时间
     */
    @Column(name = "LAST_LOGIN_TIME")
    @Comment("上次登录时间")
    private Date lastLoginTime;

    /**
     * 登录次数
     */
    @Column(name = "LOGIN_TIME")
    private Integer loginTimes;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "platformSecurityAccount")
    private Set<PersistentToken> persistentTokens;

    /**
     * 系统用户
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "PLATFORM_SYS_USER_ID")
    private PlatformSysUser platformSysUser;

    public Integer getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(Integer loginTimes) {
        this.loginTimes = loginTimes;
    }

    public Boolean getLock() {
        return isLock;
    }

    public void setLock(Boolean lock) {
        isLock = lock;
    }

    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }

    public PlatformSysUser getPlatformSysUser() {
        return platformSysUser;
    }

    public void setPlatformSysUser(PlatformSysUser platformSysUser) {
        this.platformSysUser = platformSysUser;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Boolean isExpired) {
        this.isExpired = isExpired;
    }

    public Boolean getIsLock() {
        return isLock;
    }

    public void setIsLock(Boolean isLock) {
        this.isLock = isLock;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Set<PersistentToken> getPersistentTokens() {
        return persistentTokens;
    }

    public void setPersistentTokens(Set<PersistentToken> persistentTokens) {
        this.persistentTokens = persistentTokens;
    }
}
