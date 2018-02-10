package org.poem.common.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by poem on 2016/6/18.
 * 退出登录的时候的处理类
 */
@Component
public class LogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

    /**
     * 日志管理
     */
    private static Logger logger = LoggerFactory.getLogger(LogoutSuccessHandler.class);

    /**
     * 退出登录的处理
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,Authentication authentication) throws IOException, ServletException {
        logger.info("欢迎下次再来！");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
