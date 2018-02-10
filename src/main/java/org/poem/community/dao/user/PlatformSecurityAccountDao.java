package org.poem.community.dao.user;

import org.poem.community.entity.PlatformSecurityAccount;

import org.poem.core.dao.BaseDTO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by poem on 2016/6/20.
 *  用户登录系统账号管理
 */
public interface PlatformSecurityAccountDao extends BaseDTO<PlatformSecurityAccount,String> {

    /**
     * 根据用户的登录名和密码查找
     * @param userName 用户名
     * @param password 密码
     * @return 登录的账户
     */
    @Query("SELECT ACCOUNT FROM  PlatformSecurityAccount AS ACCOUNT INNER  JOIN FETCH ACCOUNT.platformSysUser  WHERE  ACCOUNT.account=?1 AND  ACCOUNT.password=?2")
    public PlatformSecurityAccount findAccountByNameAndPwd(String userName, String password);


    /**
     * 根据用户的登录名查找
     * @param userName 用户名
     * @return 登录的账户
     */
    @Query("SELECT ACCOUNT FROM  PlatformSecurityAccount AS ACCOUNT INNER  JOIN FETCH ACCOUNT.platformSysUser  WHERE  ACCOUNT.account=?1")
    public PlatformSecurityAccount findAccountByName(String userName);
}
