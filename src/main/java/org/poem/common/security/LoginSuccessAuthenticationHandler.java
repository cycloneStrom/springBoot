package org.poem.common.security;

import org.poem.common.commonenum.LoginStatusEnum;
import org.poem.common.utils.JsonBean;
import org.poem.common.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by poem on 2016/6/18.
 *  登录的时候的处理类
 */
@Component
public class LoginSuccessAuthenticationHandler extends SimpleUrlAuthenticationSuccessHandler {

    /**
     * 日志管理
     */
    private static Logger logger = LoggerFactory.getLogger(LoginSuccessAuthenticationHandler.class);
    /**
     * 登陆成功的时候的操作
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,Authentication authentication)
            throws IOException, ServletException {
        logger.info("登陆成功@！这儿不做登录成功后的页面的自动重定向处理，页面的改变在前端使用angularJs处理");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(JsonUtils.toJson(new JsonBean<LoginStatusEnum>(
                LoginStatusEnum.SUCCESS.getMessage(), LoginStatusEnum.SUCCESS.getCode(), LoginStatusEnum.SUCCESS)));
    }
}
