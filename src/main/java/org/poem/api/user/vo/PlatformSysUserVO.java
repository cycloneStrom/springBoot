package org.poem.api.user.vo;

import org.poem.core.vo.BaseVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by poem on 2016/5/10.
 * 用户信息
 */
public class PlatformSysUserVO extends BaseVO {

    /**
     * 实际名字
     */

    private String name;

    /**
     * 用户身份证号
     */
    private String number;

    /**
     * 用户网名
     */
    private String alien;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户所拥有的权限集合
     */
    private Collection<GrantedAuthority> authorities;//用户权限集合

    /**
     * 头像
     */
    private String headUrl;

    /**
     * 生日
     */
    private String birthday;

    /**
     *  sex of the user
     */
    private Byte gender;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }


    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
