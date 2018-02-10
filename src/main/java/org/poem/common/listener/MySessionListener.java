package org.poem.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by lyw on 2016/4/13.
 * 监听Session的创建与销毁
 * 当Servlet容器启动和终止的时候都会执行
 */
/*监听器注释*/
@WebListener
public class MySessionListener implements HttpSessionListener {

    /**
     * 配置 日志管理
     */
    private static final Logger logger = LoggerFactory.getLogger(MySessionListener.class);

    /**
     * Seesion被创建的时候调用
     *
     * @param se
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.info("-- Session初始化！-- ");
    }

    /**
     * Seesion被销毁的时候调用
     *
     * @param se
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info("-- Session销毁！-- ");
    }
}
