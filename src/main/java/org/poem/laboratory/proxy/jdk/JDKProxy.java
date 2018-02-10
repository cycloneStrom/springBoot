package org.poem.laboratory.proxy.jdk;

import org.poem.laboratory.proxy.api.ProxyUser;
import org.poem.laboratory.proxy.api.ProxyUserService;
import org.poem.laboratory.proxy.impl.ProxyUserServiceImpl;
import org.poem.laboratory.proxy.jdk.utils.JDKProxyUtil;

import java.lang.reflect.Proxy;

/**
 * Created by yineng
 * name:01-BlogDemo
 * ProxyUser:lyw
 * Date 2017/1/5
 */
public class JDKProxy {
    public static void main(String[] args) {
        /**
         * 被代理对象
         */
        Object proxyObject = new ProxyUserServiceImpl();
        /**
         *  代理中间件
         */
        JDKProxyUtil jdkProxyUtil = new JDKProxyUtil(proxyObject);
        // 生成代理对象，对被代理对象的这些接口进行代理：ProxyUserServiceImpl.class.getInterfaces()
        ProxyUserService proxyUserService =
                (ProxyUserService) Proxy.newProxyInstance( Thread.currentThread().getContextClassLoader(),
                        ProxyUserServiceImpl.class.getInterfaces(),
                        jdkProxyUtil );
        proxyUserService.addUser( new ProxyUser() );
        proxyUserService.getUser( "1" );
    }
}
