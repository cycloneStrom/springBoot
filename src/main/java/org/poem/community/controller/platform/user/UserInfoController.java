package org.poem.community.controller.platform.user;

import javafx.application.Platform;
import org.hibernate.validator.constraints.NotBlank;
import org.poem.api.exception.PlatformException;
import org.poem.api.user.PlatformSysUserService;
import org.poem.api.user.vo.PlatformSecurityAccountVO;
import org.poem.api.user.vo.PlatformSysUserVO;
import org.poem.common.utils.JsonBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * Created by poem on 2016/12/25.
 * the user model controller
 */
@RestController
@RequestMapping("/action/userInfo")
public class UserInfoController {

    /**
     * the note of this class
     */
    private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    /**
     * the service od the user
     */
    @Autowired
    private PlatformSysUserService platformSysUserService;
    /**
     * query user base info by user-id
     * @param userId user id ,is not null
     * @return user base info vo
     */
    @RequestMapping("/queryPlatfromUserById")
    public JsonBean queryPlatfromUserById(@NotBlank String userId){
        PlatformSecurityAccountVO platformSecurityUserVO =(PlatformSecurityAccountVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserId = platformSecurityUserVO.getPlatformSysUserVO().getId();
        if(!userId.equals(currentUserId)){
          return new JsonBean("this user info is error .",-1);
        }
        try {
            return  new JsonBean(this.platformSysUserService.queryPlatfromUserById(userId));
        }catch(PlatformException e){
            logger.error(e.getMessage(),e);
            return new JsonBean(e.getMessage(),-1);
        }
    }

    /**
     * save the change of the user
     * @param platformSysUserVO user info
     * @return
     */
    @RequestMapping("/savePlatformSysUser")
    public JsonBean savePlatformSysUser(@NotNull  PlatformSysUserVO platformSysUserVO){
        PlatformSecurityAccountVO platformSecurityUserVO =(PlatformSecurityAccountVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserId = platformSecurityUserVO.getPlatformSysUserVO().getId();
        if(!currentUserId.equals(platformSysUserVO.getId())){
            return new JsonBean("this user info is error .",-1);
        }
        try{
            this.platformSysUserService.savePlatformSysUser(platformSysUserVO);
            return new JsonBean();
        }catch(PlatformException e){
            logger.error(e.getMessage(),e);
            return new JsonBean(e.getMessage(),-1);
        }
    }
}
