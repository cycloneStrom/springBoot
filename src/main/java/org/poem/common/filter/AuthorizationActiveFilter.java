package org.poem.common.filter;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by poem on 2016/7/2.
 * 系统激活授权拦截器
 */
public class AuthorizationActiveFilter extends OncePerRequestFilter {

    public AuthorizationActiveFilter(AuthenticationFailureHandler authenticationFailureHandler) {

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, response);
    }
}
