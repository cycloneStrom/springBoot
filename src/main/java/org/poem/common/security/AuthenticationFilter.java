package org.poem.common.security;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by poem on 2016/7/2.
 * 权限拦截器
 */
public class AuthenticationFilter  extends UsernamePasswordAuthenticationFilter {

    private List<RequestMatcher> requestMatchers;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationFilter(List<String> requestMatchers) {

        if (CollectionUtils.isNotEmpty(requestMatchers)) {
            this.requestMatchers =requestMatchers.parallelStream().map(a->(new AntPathRequestMatcher(a))).collect(Collectors.toList());
        }
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (super.requiresAuthentication(request, response)) {
            return true;
        }
        return requestMatchers.parallelStream().anyMatch(a->(a.matches(request)));
    }


}
