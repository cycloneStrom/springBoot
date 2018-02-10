package org.poem.laboratory.proxy.impl;

import org.poem.laboratory.proxy.api.ProxyUser;
import org.poem.laboratory.proxy.api.ProxyUserService;

/**
 * Created by yineng
 * name:01-BlogDemo
 * ProxyUser:lyw
 * Date 2017/1/5
 */
public class ProxyUserServiceImpl implements ProxyUserService {
    @Override
    public void addUser(ProxyUser user) {
        System.out.println("add user into database!");
    }

    @Override
    public ProxyUser getUser(String id) {
        ProxyUser user = new ProxyUser();
        user.setId( id );
        user.setName( "风雨无阻" );
        System.out.println("getUser form database!");
        return user;
    }
}
