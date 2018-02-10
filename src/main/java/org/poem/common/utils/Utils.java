package org.poem.common.utils;


import java.util.Date;

/**
 * Created by poem on 2016/4/17.
 * 基础服务类
 */
public class Utils {

    /**
     * 系统session命名
     */
    public static final String HTTP_SESSION = "org.poem.utils.http.session";


    /**
     * 当前登录用的id
     */
    public static final String HTTP_USER_ID_SESSION = "org.poem.utils.http.session.user.id";

    /**
     * cookie名称
     */
    public static final String COOKIE_NAME  = "myApp_cookies";
    /**
     * 获取一个随机数
     * @return
     */
    public static Long getRoundNum(){
        return (long)(Math.random()*new Date().getTime());
    }
}
