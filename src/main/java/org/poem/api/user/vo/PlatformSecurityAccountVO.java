package org.poem.api.user.vo;


import org.poem.core.vo.BaseVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by poem on 2016/6/20.
 * 用户的账号信息
 */
public class PlatformSecurityAccountVO extends BaseVO implements UserDetails {
    /**
     * account 账号
     */
    private String account;

    /**
     * password 密码
     */
    private String password;

    /**
     * token TOKEN
     */
    private String token;

    /**
     * isExpired 是否过期 true-是，false-否
     */
    private Boolean isExpired;

    /**
     * isLock 是否锁定 true-是，false-否
     */
    private Boolean isLock;

    /**
     * createTime 创建时间
     */
    private Date createTime;

    /**
     * status 状态：1=激活，0=冻结，-1=注销
     */
    private Byte status;

    /**
     * lastLoginTime 上次登录时间
     */
    private Date lastLoginTime;

    /**
     * 权限集合
     */
    private List<GrantedAuthority> grantedAuthorities;
    /**
     * 登录账号的信息
     */
    private PlatformSysUserVO platformSysUserVO;

    public PlatformSysUserVO getPlatformSysUserVO() {
        return platformSysUserVO;
    }

    public void setPlatformSysUserVO(PlatformSysUserVO platformSysUserVO) {
        this.platformSysUserVO = platformSysUserVO;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public List<GrantedAuthority> getGrantedAuthorities() {
        return grantedAuthorities;
    }

    public void setGrantedAuthorities(List<GrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        if (platformSysUserVO != null) {
            return platformSysUserVO.getName();
        } else {
            throw new RuntimeException("this user info is not null");
        }
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
