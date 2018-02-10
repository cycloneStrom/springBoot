package org.poem.common.security;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by poem on 2016/6/18.
 * 跳转路径
 */
public interface DirectUrlResolver {

    /**
     * 获得配置配置标识用以跳转判断.
     *
     * @param request the request
     * @return true, if successful
     */
    boolean support(HttpServletRequest request);

    /**
     * 获取跳转路径.
     *
     * @return the string
     */
    String directUrl();
}
