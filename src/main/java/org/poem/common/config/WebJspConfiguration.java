//package org.poem.common.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Description;
//import org.springframework.core.env.Environment;
//import org.springframework.web.servlet.view.InternalResourceViewResolver;
//
///**
// * Created by poem on 2016/6/24.
// * Web服务配置 系统加载jsp模板
// */
//@Configuration
//public class WebJspConfiguration {
//
//    @Autowired
//    private Environment env;
//
//    /**
//     * 加载jsp模板
//     * @return
//     */
//    @Bean
//    @Description("loading Jsp mode environment.......")
//    public InternalResourceViewResolver jspResourceView() {
//        InternalResourceViewResolver internalResourceViewResolver=new InternalResourceViewResolver();
//        internalResourceViewResolver.setPrefix(env.getProperty("spring.view.prefix"));
//        internalResourceViewResolver.setSuffix(env.getProperty("spring.view.suffix"));
//        internalResourceViewResolver.setViewNames(env.getProperty("spring.view.view-names"));
//        internalResourceViewResolver.setCache(Boolean.valueOf(env.getProperty("spring.view.cache")));
//        internalResourceViewResolver.setOrder(1);
//
//        return internalResourceViewResolver;
//    }
//}
