package org.poem.community.controller.platform.login;

import org.apache.commons.lang3.StringUtils;
import org.poem.api.common.vo.FaildInfoVO;
import org.poem.api.exception.PlatformException;
import org.poem.api.user.PlatformSecurityAccountService;
import org.poem.api.user.vo.PlatformSecurityAccountVO;
import org.poem.api.user.vo.RegistePlatformSecurityAccountVO;
import org.poem.common.utils.JsonBean;
import org.poem.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by poem on 2016/4/14.
 * 登录控制
 */
@Controller
@RequestMapping(value = "/api/login")
public class LoginController {

    /**
     * 配置  日志管理
     */
    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * 登录对象
     */
    @Resource
    private AuthenticationManager authenticationManager;

    /**
     * 账号管理
     */
    @Autowired
    private PlatformSecurityAccountService platformSecurityAccountService;
    /**
     * 实现登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录信息
     */
    @RequestMapping(value = "/loginForm", method = RequestMethod.POST,headers = "Accept=application/json")
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public JsonBean loginForm(HttpServletRequest request, HttpServletResponse response, String username, String password) throws IOException {
        SecurityContextHolder.clearContext();
        if (StringUtils.isBlank(username)) {
            return new JsonBean("用户名为空！", -1);
        }
        if (StringUtils.isBlank(password)) {
            return new JsonBean("密码为空", -1);
        }
        HttpSession session = request.getSession();
        int status = -1;
        String loginMessage = toLogin(username,password);
        if(StringUtils.isBlank(loginMessage)){
            /**获取当前的登录人的信息*/
            PlatformSecurityAccountVO platformSecurityUserVO =(PlatformSecurityAccountVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userName  = platformSecurityUserVO.getPlatformSysUserVO().getName();
            status = 0;
            session.setMaxInactiveInterval(-1);
            response.addCookie(new Cookie(Utils.COOKIE_NAME,userName));
        }
        return new JsonBean(loginMessage,status);
    }

    /**
     * 登录信息管理
     * @param userName 用户
     * @param password 密码
     * @return 登录信息
     */
    private String toLogin(String userName,String password){
        String errorMessage = null;
        try {
            /*登录授权管理*/
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userName, password);
            Authentication auth = this.authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }catch (LockedException e) {
            errorMessage = "该登录账号已锁定，请联系管理员！";
            logger.error(errorMessage, e);
        } catch (DisabledException e) {
            errorMessage = "登录账号不存在！";
            logger.error(errorMessage, e);
        } catch (BadCredentialsException e) {
            errorMessage = e.getMessage();
            logger.error(errorMessage, e);
        }
        return errorMessage;
    }


    /**
     * 注册账号
     * @param registePlatformSecurityAccountVO
     * @return
     */
    @RequestMapping(name = "/regiestAccount", method = RequestMethod.POST,headers = "Accept=application/json")
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public JsonBean regiestAccount(RegistePlatformSecurityAccountVO registePlatformSecurityAccountVO){
        FaildInfoVO faildInfoVO ;
        try{
            faildInfoVO = this.platformSecurityAccountService.registePlatformSecurityAccount(registePlatformSecurityAccountVO);
            return new JsonBean(faildInfoVO);
        }catch (PlatformException e){
            logger.info(e.getStatusMessage(),e);
            return new JsonBean(e.getMessage(),-1);
        }
    }
}
