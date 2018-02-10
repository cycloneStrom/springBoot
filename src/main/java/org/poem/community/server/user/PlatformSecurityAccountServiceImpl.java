package org.poem.community.server.user;

import org.poem.api.common.vo.FaildInfoVO;
import org.poem.api.exception.PlatformException;
import org.poem.api.user.PlatformSecurityAccountService;
import org.poem.api.user.vo.PlatformSecurityAccountVO;
import org.poem.api.user.vo.PlatformSysUserVO;
import org.poem.api.user.vo.RegistePlatformSecurityAccountVO;
import org.poem.common.utils.StringUtils;
import org.poem.community.dao.user.PlatformSecurityAccountDao;
import org.poem.community.dao.user.PlatformSysUserDao;
import org.poem.community.entity.PlatformSecurityAccount;
import org.poem.community.entity.PlatformSysUser;
import org.poem.core.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by poem on 2016/6/20.
 * 账户管理
 */
@Service
public class PlatformSecurityAccountServiceImpl implements PlatformSecurityAccountService {


    /**
     * 用户账户管理
     */
    @Autowired
    private PlatformSecurityAccountDao platformSecurityAccountDao;

    /**
     * 用户
     */
    @Autowired
    private PlatformSysUserDao platformSysUserDao;

    /**
     * 重写用户的登录模块的信息
     *
     * @param userName 用户名
     * @param password 密码
     * @return 查询到的信息
     * @throws UsernameNotFoundException
     */
    @Override
    public PlatformSecurityAccountVO loadUserByUsernameAndPwd(String userName, String password) throws UsernameNotFoundException, PlatformException {
        if (StringUtils.isBlank(password) || StringUtils.isBlank(userName)) {
            throw new PlatformException("用户名或密码不能为空！");
        }
        PlatformSecurityAccount platformSecurityAccount = this.platformSecurityAccountDao.findAccountByNameAndPwd(userName, password);
        PlatformSecurityAccountVO platformSecurityAccountVO = new PlatformSecurityAccountVO();
        PlatformSysUserVO platformSysUserVO = new PlatformSysUserVO();
        if (platformSecurityAccount == null) {
            throw new UsernameNotFoundException("the user is not found");
        }
        BeanUtils.copyProperties(platformSecurityAccount, platformSecurityAccountVO);
        PlatformSysUser platformSysUser = platformSecurityAccount.getPlatformSysUser();
        BeanUtils.copyProperties(platformSysUser, platformSysUserVO);
        platformSecurityAccount.setLastLoginTime(new Date());
        if (platformSecurityAccount.getLoginTimes() == null) {
            platformSecurityAccount.setLoginTimes(1);
        } else {
            platformSecurityAccount.setLoginTimes(platformSecurityAccount.getLoginTimes() + 1);
        }
        this.platformSecurityAccountDao.save(platformSecurityAccount);
        platformSecurityAccountVO.setPlatformSysUserVO(platformSysUserVO);
        return platformSecurityAccountVO;
    }

    /**
     * 注册账号
     *
     * @param registePlatformSecurityAccountVO
     * @return
     * @throws PlatformException
     */
    @Override
    public FaildInfoVO registePlatformSecurityAccount(RegistePlatformSecurityAccountVO registePlatformSecurityAccountVO) throws PlatformException {
        if (StringUtils.isEmpty(registePlatformSecurityAccountVO.getUserName())) {
            throw new PlatformException("用户名不能为空！");
        }
        if (StringUtils.isEmpty(registePlatformSecurityAccountVO.getPassword())) {
            throw new PlatformException("密码不能为空！");
        }
        FaildInfoVO faildInfoVO = validateRegist(registePlatformSecurityAccountVO);
        if(null == faildInfoVO){
            savePlatformSysUser(registePlatformSecurityAccountVO);
        }
        return faildInfoVO;
    }

    /**
     * 验证是否可以注册
     * @param registePlatformSecurityAccountVO
     * @return
     */
    private FaildInfoVO validateRegist(RegistePlatformSecurityAccountVO registePlatformSecurityAccountVO){
        FaildInfoVO faildInfoVO = null ;
        PlatformSecurityAccount platformSecurityAccounts = platformSecurityAccountDao.findAccountByName(registePlatformSecurityAccountVO.getUserName());
        if(null != platformSecurityAccounts){
            faildInfoVO = new FaildInfoVO();
            faildInfoVO.setFailedReason("当前账号已被注册.");
        }
        return faildInfoVO;
    }

    /**
     * 保存用户信息
     *
     * @param registePlatformSecurityAccountVO
     */
    private void savePlatformSysUser(RegistePlatformSecurityAccountVO registePlatformSecurityAccountVO) {
        PlatformSysUser platformSysUser = new PlatformSysUser();
        platformSysUser.setName(registePlatformSecurityAccountVO.getUserName());
        platformSysUser.setIsDel(false);
        platformSysUser.setGender(Byte.valueOf(registePlatformSecurityAccountVO.getSex()));
        platformSysUser.setAlien(registePlatformSecurityAccountVO.getUserName());
        platformSysUser.setCreateDateTime(new Date());
        platformSysUser.setCreateDateTime(new Date());
        savePlatformSysSecurityAccount(platformSysUserDao.save(platformSysUser), registePlatformSecurityAccountVO.getPassword());
    }

    /**
     * 保存账号
     *
     * @param platformSysUser
     * @param password
     */
    private void savePlatformSysSecurityAccount(PlatformSysUser platformSysUser, String password) {
        PlatformSecurityAccount account = new PlatformSecurityAccount();
        account.setPlatformSysUser(platformSysUser);
        account.setAccount(platformSysUser.getName());
        account.setIsExpired(false);
        account.setIsLock(false);
        account.setCreateTime(new Date());
        account.setStatus(Byte.valueOf("1"));
        this.platformSecurityAccountDao.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }


}
