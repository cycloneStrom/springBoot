package org.poem.common.security;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by poem on 2016/6/18.
 * 实现资源的拦截等信息
 * 定义成组件
 */
@Component
public class SourceSecurityAuthenticationEntry implements AuthenticationEntryPoint{

    /**
     * 默认的登录
     */
    private static String  defaultLoginUrl = "/login.htm";


    /**
     * 路径
     */
    private List<DirectUrlResolver> directUrlResolvers = Lists.newArrayList();


    /**
     * 根据输入路径与配置项得到跳转路径分别跳到不同登录页面.
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //返回自定义状态码
        String header = httpServletRequest.getHeader("X-Requested-With");
        /*判断是不是ajax请求，如果是，没有授权信息就反应会399登录状态*/
        if(StringUtils.isNotBlank(header) && header.equals("XMLHttpRequest")) {
            httpServletResponse.setStatus(399);
        }else{
            String ctxPath = httpServletRequest.getContextPath();
            for (DirectUrlResolver directUrlResolver : directUrlResolvers) {
                if (directUrlResolver.support(httpServletRequest) ){
                    String loginUrl = directUrlResolver.directUrl();
                    httpServletResponse.sendRedirect(ctxPath + loginUrl);
                    return;
                }
            }
            httpServletResponse.sendRedirect(ctxPath + defaultLoginUrl);
        }
    }


    public List<DirectUrlResolver> getDirectUrlResolvers() {
        return directUrlResolvers;
    }

    public void setDirectUrlResolvers(List<DirectUrlResolver> directUrlResolvers) {
        this.directUrlResolvers = directUrlResolvers;
    }
}
