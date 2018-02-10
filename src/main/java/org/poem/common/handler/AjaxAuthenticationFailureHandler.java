package org.poem.common.handler;

import org.poem.api.exception.SystemExpiredException;
import org.poem.api.exception.SystemNotActivatedException;
import org.poem.common.commonenum.LoginStatusEnum;
import org.poem.common.utils.JsonBean;
import org.poem.common.utils.JsonUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by poem on 2016/7/2.
 * url请求错误的处理类
 */
public class AjaxAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public AjaxAuthenticationFailureHandler(String defaultFailureUrl) {
        super(defaultFailureUrl);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof SystemNotActivatedException) {
            response.getWriter().println(JsonUtils.toJson(new JsonBean(LoginStatusEnum.ACTIVATE.getMessage(), LoginStatusEnum.ACTIVATE.getCode(), LoginStatusEnum.ACTIVATE)));
        } else if (exception instanceof SystemExpiredException) {
            super.onAuthenticationFailure(request, response, exception);
        } else {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
            response.getWriter().println(JsonUtils.toJson(new JsonBean(LoginStatusEnum.FAILED.getMessage(), LoginStatusEnum.FAILED.getCode(), LoginStatusEnum.FAILED)));
        }
    }
}
