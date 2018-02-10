package org.poem.common.filter;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by poem on 2016/7/2.
 * 系统失效验证拦截
 */
public class AuthorizationExpiredFilter extends OncePerRequestFilter {
    public AuthorizationExpiredFilter(AuthenticationFailureHandler authenticationFailureHandler) {

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, response);
    }
}
