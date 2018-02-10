package org.poem.common.config;

import org.poem.common.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PreDestroy;

/**
* Created by poem on 2016/6/24.
* 使用redis 做 缓存配置
*/

@Configuration
@EnableCaching
@AutoConfigureAfter(value = {MetricsConfiguration.class, DatabaseConfiguration.class})
@Profile("!" + Constants.SPRING_PROFILE_FAST)
public class CacheConfiguration extends CachingConfigurerSupport {

    /**
     * 日志管理
     */
    private final Logger log = LoggerFactory.getLogger(CacheConfiguration.class);

    /**
     * 銷毀的操作
     */
    @PreDestroy
    public void destroy() {
        log.info("Closing Cache Manager");
    }


    /**
     * key
     * @return
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return (target,method,params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }
}
