package org.poem.laboratory.proxy.cglib.util;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by yineng
 * name:01-BlogDemo
 * User:lyw
 * Date 2017/1/5
 */
public class CGProxyUtil implements MethodInterceptor {

    /**
     * 被代理的对象
     */
    private Object target;

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("do sth before ......");
        Object result = methodProxy.invokeSuper( o,objects );
        System.out.println("do sth after .......");
        return result;
    }

    public CGProxyUtil(Object target){
        this.target = target;
    }
    public Object getProxyObject (){
        Enhancer enhancer = new Enhancer();
        /**设置父类*/
        enhancer.setSuperclass( this.target.getClass() );
        /**设置回调 在调用父类方法时，回调 this.intercept()*/
        enhancer.setCallback( this );
        /*创建代理对象*/
        return  enhancer.create();
    }
}
