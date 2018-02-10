package org.poem.api.user;


import org.poem.api.exception.PlatformException;
import org.poem.api.user.vo.PlatformSysUserVO;
import org.poem.community.entity.PlatformSysUser;

/**
 * Created by poem on 2016/5/10.
 * 用户的表
 */
public interface PlatformSysUserService {

    /**
     * 获取用户的详细的信息
     * @param userId 用户信息id
     * @return 用户信息
     */
    public PlatformSysUserVO queryPlatfromUserById(String userId) throws PlatformException;

    /**
     * 保存用户详细
     * @throws PlatformException
     */
    public void savePlatformSysUser(PlatformSysUserVO platformSysUserVO) throws PlatformException;
}
