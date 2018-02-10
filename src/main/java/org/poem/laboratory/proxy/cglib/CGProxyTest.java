package org.poem.laboratory.proxy.cglib;

import org.poem.laboratory.proxy.api.ProxyUser;
import org.poem.laboratory.proxy.api.ProxyUserService;
import org.poem.laboratory.proxy.cglib.util.CGProxyUtil;
import org.poem.laboratory.proxy.impl.ProxyUserServiceImpl;

/**
 * Created by yineng
 * name:01-BlogDemo
 * User:lyw
 * Date 2017/1/5
 */
public class CGProxyTest {
    public static void main(String[] args) {
        /**被代理的对象*/
        Object proxyObject = new ProxyUserServiceImpl();
        // 生成代理对象，对被代理对象的这些接口进行代理：ProxyUserServiceImpl.class.getInterfaces()
        CGProxyUtil cgProxyUtil = new CGProxyUtil(proxyObject);
        ProxyUserService proxyUserService = (ProxyUserService) cgProxyUtil.getProxyObject();
        proxyUserService.addUser( new ProxyUser() );
        proxyUserService.getUser( "1" );
    }
}
