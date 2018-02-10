package org.poem.community.server.user;


import org.apache.commons.lang3.StringUtils;
import org.poem.api.exception.PlatformException;
import org.poem.api.user.PlatformSysUserService;
import org.poem.api.user.vo.PlatformSysUserVO;
import org.poem.common.utils.DateUtils;
import org.poem.community.dao.user.PlatformSysUserDao;
import org.poem.community.entity.PlatformSysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * Created by poem on 2016/5/10.
 * 用户的查询
 */
@Service
public class PlatformSysUserServiceImpl implements PlatformSysUserService {

    /**
     * 日志管理
     */
    private static final Logger logger = LoggerFactory.getLogger(PlatformSysUserServiceImpl.class);

    /**
     * the user of ODT
     */
    @Autowired
    private PlatformSysUserDao platformSysUserDao;
    /**
     *
     * @param userId 用户信息id
     * @return
     * @throws PlatformException
     */
    @Override
    public PlatformSysUserVO queryPlatfromUserById(String userId) throws PlatformException {
        if(StringUtils.isBlank(userId)){
            throw new PlatformException("give id is not null");
        }
        logger.info("query user by id is :" + userId);
        PlatformSysUser platformSysUser = this.platformSysUserDao.findOne(userId);
        PlatformSysUserVO platformSysUserVO = new PlatformSysUserVO();
        platformSysUserVO.setId(platformSysUser.getId());
        platformSysUserVO.setAlien(platformSysUser.getAlien());
        platformSysUserVO.setEmail(platformSysUser.getEmail());
        platformSysUserVO.setHeadUrl(platformSysUser.getHeadUrl());
        platformSysUserVO.setName(platformSysUser.getName());
        platformSysUserVO.setNumber(platformSysUser.getNumber());
        platformSysUserVO.setGender(platformSysUser.getGender());
        if(null != platformSysUser.getBirthday()){
            platformSysUserVO.setBirthday( DateUtils.formatDate( platformSysUser.getBirthday() ) );
        }
        return platformSysUserVO;
    }

    /**
     *
     * @param platformSysUserVO
     * @throws PlatformException
     */
    @Override
    public void savePlatformSysUser(PlatformSysUserVO platformSysUserVO) throws PlatformException {
        if (StringUtils.isBlank(platformSysUserVO.getId())){
            throw new PlatformException("this user is error ");
        }
        logger.info("save user by id is :" + platformSysUserVO.getId());
        PlatformSysUser platformSysUser = this.platformSysUserDao.findOne(platformSysUserVO.getId());
        if(null == platformSysUser){
            throw  new PlatformException("don't found this user ");
        }
        platformSysUser.setGender(platformSysUserVO.getGender());
        platformSysUser.setName(platformSysUserVO.getName());
        platformSysUser.setHeadUrl(platformSysUserVO.getHeadUrl());
        platformSysUser.setEmail(platformSysUserVO.getEmail());
        platformSysUser.setNumber(platformSysUserVO.getNumber());
        platformSysUser.setModifyDateTime(new Date());
        if(StringUtils.isNotEmpty( platformSysUserVO.getBirthday() )){
            platformSysUser.setBirthday( DateUtils.parseDate( platformSysUserVO.getBirthday() ) );
        }
        platformSysUser.setAlien(platformSysUserVO.getAlien());
        this.platformSysUserDao.save(platformSysUser);
    }
}
