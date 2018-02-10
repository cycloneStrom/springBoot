package org.poem.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by lyw on 2016/4/13.
 * servlet上下文生命周期监听器
 * 监视web应用的生命周期
 * 当Servlet容器启动和终止的时候都会执行
 */
/*监听器注释*/
@WebListener
public class MyServletContentListener implements ServletContextListener {


    private static final Logger logger = LoggerFactory.getLogger(MyServletContentListener.class);

    /**
     * 当Servlet 容器启动Web 应用时调用该方法。在调用完该方法之后，容器再对Filter 初始化，
     * 并且对那些在Web 应用启动时就需要被初始化的Servlet 进行初始化。
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("-- 初始化servlet --");
    }


    /**
     * 当Servlet 容器终止Web 应用时调用该方法。在调用该方法之前，容器会先销毁所有的Servlet 和Filter 过滤器。
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("-- 销毁servlet --");
    }
}
