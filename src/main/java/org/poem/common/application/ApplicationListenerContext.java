package org.poem.common.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.RequestHandledEvent;

/**
 * Created by yineng
 * name:01-BlogDemo
 * User:lyw
 * Date 2016/12/29
 */
@Service
public class ApplicationListenerContext implements ApplicationListener {
    /**
     * 记录
     */
    private static final Logger logger = LoggerFactory.getLogger(ApplicationListenerContext.class  );

    /**
     *
     * @param applicationEvent
     */
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if(applicationEvent instanceof ContextRefreshedEvent){
            logger.info( "*************************************************** ContextRefreshedEvent" );
        }
        if(applicationEvent instanceof ContextStartedEvent){
            logger.info( "*************************************************** ContextStartedEvent" );
        }
        if(applicationEvent instanceof ContextClosedEvent){
            logger.info( "*************************************************** ContextClosedEvent" );
        }
        if(applicationEvent instanceof RequestHandledEvent){
            RequestHandledEvent requestHandledEvent = (RequestHandledEvent)applicationEvent;
            System.err.println( requestHandledEvent.getDescription() );
            System.err.println( requestHandledEvent.getSessionId() );
            System.err.println( requestHandledEvent.getUserName() );
            logger.info( "*************************************************** RequestHandledEvent" );
        }
    }
}
