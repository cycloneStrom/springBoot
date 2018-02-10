package org.poem.community.controller.platform;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
import org.poem.api.user.vo.PlatformSecurityAccountVO;
import org.poem.common.utils.JsonBean;
import org.poem.common.utils.PropertyUtils;
import org.poem.common.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lyw on 2016/4/13.
 * 创建登录
 */
@Controller
@Timed
@RequestMapping("/")
public class PlatformController {

    /**
     * 日志配置管理
     */
    private static final  Logger logger = LoggerFactory.getLogger(PlatformController.class);

    /** 登录错误次数太多的key值. */
    private static final String IS_HIDE_RAND_CODE = "isHideRandCode";


    private static final String LOGIN_FAILED_TIMES_KEY ="platform.user.service.login.failed.times";

    /** 不需要验证码的登录，最大允许登录失败次数. */
    private static final int LOGIN_ERR_TRY_COUNT = PropertyUtils.getInt("LOGIN_TRY_ERR_COUNT", 3);

    /**
     * 获取首页的信息
     * @param request 请求
     * @param response 反馈
     * @return 空
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/login")
    public String getIndex(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (null==principal || principal instanceof String) {
            request.getSession().invalidate();
        }
        if (isLoginErrMore(request.getRemoteHost())) {
            request.setAttribute(IS_HIDE_RAND_CODE, false);
        } else {
            request.setAttribute(IS_HIDE_RAND_CODE, true);
        }
        logger.info("梦想从这儿开始");
        /*反正只要对这个也页面有请求，则一律清除session 重新登录*/
        SecurityContextHolder.clearContext();
        return "/pages/platform/login/login";
    }


    /**
     * 判断当前用户是否登录失败次数过多.
     * @param ip 当前登录用户IP地址
     * @return 登录次数超过设定的值，返回ture，否则为false
     */
    private boolean isLoginErrMore(String ip) {
        Integer tryCount = getIPLogFailedTimes(ip);
        return tryCount != null && tryCount >= LOGIN_ERR_TRY_COUNT;
    }

    /**
     * 保存的数据
     * @param ip
     * @return
     */
    private Integer getIPLogFailedTimes(String ip) {
        if (StringUtils.isBlank(ip)) {
            return null;
        }
        Object obj = RedisUtils.hashGet(LOGIN_FAILED_TIMES_KEY, ip);
        if (null != obj) {
            return (Integer) obj;
        }
        return null;
    }

    /**
     * 错误页面
     * @param request
     * @return
     */
    @RequestMapping("/error")
    public String error(HttpServletRequest request){
        SecurityContextHolder.clearContext();
        return "/pages/platform/error/error";
    }

    /**
     * 聊天界面
     * @return
     */
    @RequestMapping("/chat")
    public String chatTest(){
        return "/pages/socktjs/sockt";
    }
}
