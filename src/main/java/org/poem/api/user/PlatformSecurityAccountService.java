package org.poem.api.user;

import org.poem.api.common.vo.FaildInfoVO;
import org.poem.api.exception.PlatformException;
import org.poem.api.user.vo.PlatformSecurityAccountVO;
import org.poem.api.user.vo.RegistePlatformSecurityAccountVO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by poem on 2016/6/20.
 * 用户账号管理接口
 */
public interface PlatformSecurityAccountService  extends UserDetailsService {

    /**
     * 用户登录几口
     * @param userName 用户账号
     * @param password 用户密码
     * @return 查询到的用户信息
     * @throws UsernameNotFoundException
     */
    public PlatformSecurityAccountVO loadUserByUsernameAndPwd(String userName,String password) throws UsernameNotFoundException, PlatformException;


    /**
     * 注册账号
     * @param registePlatformSecurityAccountVO
     * @return
     * @throws PlatformException
     */
    public FaildInfoVO registePlatformSecurityAccount(RegistePlatformSecurityAccountVO registePlatformSecurityAccountVO) throws PlatformException;
}
