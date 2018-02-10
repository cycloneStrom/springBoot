package org.poem.community.controller.common;

import org.poem.api.user.vo.PlatformSecurityAccountVO;
import org.poem.common.utils.JsonBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by poem on 2016/6/27.
 */
@Controller
@RequestMapping("/commonController")
public class CommonController {

    /**
     * 日志管理
     */
    private static Logger logger = LoggerFactory.getLogger(CommonController.class);


    /**
     * 获取当前的登录人的信息
     * @return 当前登录人的信息
     */
    @RequestMapping("/queryForLoginInfoFactory")
    @ResponseBody
    public JsonBean queryForLoginInfoFactory(){
        PlatformSecurityAccountVO platformSecurityAccountVO = (PlatformSecurityAccountVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("获取当前登录人信息！");
        return new JsonBean(platformSecurityAccountVO);
    }
}
