package org.poem.laboratory.proxy.jdk.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理中间件
 * Created by yineng
 * name:01-BlogDemo
 * ProxyUser:lyw
 * Date 2017/1/5
 */
public class JDKProxyUtil implements InvocationHandler {

    /**
     * 被代理的对象
     */
    private Object target;


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("do sth before .......");
        Object result = method.invoke( target, args );
        System.out.println("do sth after ........\n");
        return result;
    }

    public JDKProxyUtil (Object target){
        this.target = target;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
