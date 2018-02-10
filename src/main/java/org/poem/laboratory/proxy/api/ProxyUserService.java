package org.poem.laboratory.proxy.api;

/**
 * Created by yineng
 * name:01-BlogDemo
 * ProxyUser:lyw
 * Date 2017/1/5
 */
public interface ProxyUserService {

    public void addUser(ProxyUser user) ;

    public ProxyUser getUser(String id);
}
