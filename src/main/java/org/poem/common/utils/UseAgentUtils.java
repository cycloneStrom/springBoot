package org.poem.common.utils;

import org.poem.core.vo.BaseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by poem on 2016/7/3.
 * 用户的客户端信息获取
 */
public class UseAgentUtils {

    /**
     * 日志管理
     */
    private static Logger logger = LoggerFactory.getLogger(UseAgentUtils.class);

    /**
     * 复制
     * @param baseVO  基础类
     * @param request 请求
     */
    public static void setUseAgentInfo(BaseVO baseVO ,HttpServletRequest request){
        if(request == null || baseVO == null){
            throw new NullPointerException();
        }
        /*用户地址*/
        String ipAddress = request.getHeader("x-forwarded-for");
        if(StringUtils.isBlank(ipAddress)){
            ipAddress = request.getRemoteAddr();
        }
        /*客户端类型的类型*/
        String userAgent = request.getHeader("User-Agent").toLowerCase();
        String clientType = null;
        if (userAgent.contains("Android")) {
            clientType =  "android";
        } else if (userAgent.contains("iPhone")) {
            clientType =  "iphone";
        } else if (userAgent.contains("compatible")) {
            clientType =  "winPhone";
        } else {
            clientType =  "电脑" ;
        }
        /*用户的浏览器信息*/
        String browserName = null;
        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("msie")) {
            browserName = "Microsoft Internet Explorer";
        }
        else if (userAgent.contains("opera")) {
            browserName  = "Opera Browser";
        }
        else if (userAgent.contains("safari")) {
            browserName = "Safari Browser";
        }
        else if (userAgent.contains("applewebkit")) {
            browserName = "Chrome Browser";
        }
        // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
        else if (userAgent.contains("mozilla")) {
            browserName = "FireFox Browser";
        }
        logger.info("ip=" + ipAddress + ",clientType=" + clientType + ",browserName=" + browserName);

        baseVO.setAgentType(clientType);
        baseVO.setBrowserMsg(browserName);
        baseVO.setIpAddress(ipAddress);
    }
}
