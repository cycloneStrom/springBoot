package org.poem.common.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by poem on 2016/4/13.
 * Spring Boot 过滤器
 * 实现对请求的过滤
 */
/*
    1、@controller 控制器（注入服务）
    2、@service 服务（注入dao）
    3、@repository dao（实现dao访问）
*/
/*Component  组件的意思，把普通pojo实例化到spring容器中，相当于配置文件中的<bean id="" class=""/> 当不好归类的时候使用这个*/
@Component
/*配置 过滤的类型*/
@WebServlet(urlPatterns = "*/**.htm")
public class DecisionManagerFilter implements Filter {


    /**
     * 配置  日志记录
     */
    private static final Logger logger = LoggerFactory.getLogger( DecisionManagerFilter.class);

    /**
     * 初始化的时候调用
     *
     * @param filterConfig 过滤器配置
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("-- 过滤器初始化！-- ");
    }

    /**
     * 实现监听的方法
     *
     * @param request  请求
     * @param response 返回
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        logger.info("FullUrl=" + ((HttpServletRequest) request).getRequestURL());
        logger.info("URL=" + ((HttpServletRequest) request).getRequestURL());
        logger.info("URI=" + ((HttpServletRequest) request).getRequestURI());
        logger.info("CTX=" + ((HttpServletRequest) request).getContextPath());
        chain.doFilter(request, response);
    }

    /**
     * 监听销毁的时候调用
     */
    @Override
    public void destroy() {
        logger.info("-- 过滤器销毁！");
    }
}
