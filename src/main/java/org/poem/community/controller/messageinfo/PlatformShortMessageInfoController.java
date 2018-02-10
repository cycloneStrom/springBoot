package org.poem.community.controller.messageinfo;

import com.codahale.metrics.annotation.Timed;
import org.poem.api.classify.PlatformSysClassifyService;
import org.poem.api.common.CustomPageableImpl;
import org.poem.api.exception.PlatformException;
import org.poem.api.messageinfo.PlatformShortMessageInfoServer;
import org.poem.api.messageinfo.vo.PlatformShortMessageInfoVO;
import org.poem.api.messageinfo.vo.PlatformShortMsgConditons;
import org.poem.api.user.vo.PlatformSecurityAccountVO;
import org.poem.common.utils.JsonBean;
import org.poem.common.utils.UseAgentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by poem on 2016/5/28.
 * 用户消息的查询
 */
@Controller
@Timed
@RequestMapping("/business/platformShortMessageInfo")
public class PlatformShortMessageInfoController  {

    /**
     * 日志管理
     */
    private static Logger logger = LoggerFactory.getLogger(PlatformShortMessageInfoController.class);

    /**
     * 消息管理
     */
    @Autowired
    private PlatformShortMessageInfoServer platformShortMessageInfoServer;

    @Autowired
    private PlatformSysClassifyService sysClassifyService;


    /**
     * 查询当前用户的消息
     * @param pageSize 分页
     * @param pageNumber 分页
     * @return 查询结果
     */
    @RequestMapping("/queryShortMsg")
    @ResponseBody
    public JsonBean queryShortMsg(Integer pageSize,Integer pageNumber,PlatformShortMsgConditons platformShortMsgConditons){
        logger.info("参数【pageSize】 " + pageSize + " 【pageNumber】" + pageNumber);
        Pageable pageable = new CustomPageableImpl(pageNumber,pageSize);
        Page<PlatformShortMessageInfoVO> platformShortMessageInfoVOs = null;
        try{
            platformShortMessageInfoVOs = this.platformShortMessageInfoServer.queryShortMessageByConditionOrderCreateDate(platformShortMsgConditons,pageable);
            return new JsonBean(platformShortMessageInfoVOs);
        }catch (PlatformException e){
            logger.error(e.getMessage(),e);
            return new JsonBean(e.getMessage(),-1);
        }
    }

    /**
     * 保存用户的信息
     * @param request 请求
     * @param platformShortMessageInfoVO 用户的消息提
     * @return 保存的信息
     */
    @RequestMapping("/saveShortMsg")
    @ResponseBody
    public JsonBean saveShortMsg(HttpServletRequest request,PlatformShortMessageInfoVO platformShortMessageInfoVO){
        /**获取当前的登录人的信息*/
        PlatformSecurityAccountVO platformSecurityUserVO =(PlatformSecurityAccountVO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        platformShortMessageInfoVO.setPlatformSysUser(platformSecurityUserVO.getPlatformSysUserVO());
        UseAgentUtils.setUseAgentInfo(platformShortMessageInfoVO,request);
        try{
            return new JsonBean(this.platformShortMessageInfoServer.saveShortMessage(request,platformShortMessageInfoVO));
        }catch (PlatformException e){
            logger.error(e.getMessage(),e);
            return new JsonBean("保存失败！",-1);
        }
    }


    /**
     * 删除消息
     * @param infoId
     * @return
     */
    @RequestMapping("/deleteMsg")
    @ResponseBody
    public JsonBean deleteMsg(String infoId){
        try {
            sysClassifyService.deleteMsg(infoId);
        } catch (PlatformException e) {
            logger.error(e.getMessage(),e);
            return new JsonBean("删除失败！",-1);
        }
        return new JsonBean();
    }


}
