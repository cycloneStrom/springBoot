package org.poem.common.filter;

import org.poem.common.filter.gzip.GZipResponseUtil;
import org.poem.common.filter.gzip.GZipServletResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * 全站压缩过滤器
 */
public class GZipServletFilter implements Filter {

    private Logger log = LoggerFactory.getLogger( GZipServletFilter.class );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing to initialize
    }

    @Override
    public void destroy() {
        // Nothing to destroy
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        GZipResponseUtil.filterGZipHanlder( request, response, chain, httpRequest, httpResponse );
    }
}
