package org.poem.community.controller.platform.classify;

import com.codahale.metrics.annotation.Timed;
import org.poem.api.classify.PlatformSysClassifyService;
import org.poem.api.classify.vo.PlatformSysClassifyConditions;
import org.poem.api.classify.vo.PlatformSysClassifyVO;
import org.poem.api.common.CustomPageableImpl;
import org.poem.api.exception.PlatformException;
import org.poem.common.utils.JsonBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by poem on 2016/5/6.
 * 类型controller
 */
@Controller
@Timed
@RequestMapping("/business/classifyController")
public class ClassifyController {

    /**
     * 日志管理
     */
    private Logger logger = LoggerFactory.getLogger(ClassifyController.class);

    /**
     * 接口
     */
    @Autowired
    private PlatformSysClassifyService platformSysClassifyService;

    /**
     * 根据条件查询记录
     * @param pageSize 大小
     * @param pageNumber 页数
     * @param platformSysClassifyConditions 查询条件
     * @return
     */
    @RequestMapping("/queryClassifyByCondition")
    @ResponseBody
    public JsonBean queryClassifyByCondition(Integer pageSize,Integer pageNumber,PlatformSysClassifyConditions platformSysClassifyConditions){
        logger.info("参数【pageSize】 " + pageSize + " 【pageNumber】" + pageNumber);
        Pageable pageable = new CustomPageableImpl(pageNumber,pageSize);
        Page<PlatformSysClassifyVO> platformSysClassifyVOs = null;
        try{
            platformSysClassifyVOs = this.platformSysClassifyService.queryPlatformSysClassifyByCondition(pageable, platformSysClassifyConditions);
            return new JsonBean(platformSysClassifyVOs);
        }catch (PlatformException e){
            logger.debug(e.getMessage(),e);
            return new JsonBean(e.getMessage(),-1);
        }
    }

    /**
     * 保存数据
     * @param platformSysClassifyVO 条件
     * @return
     */
    @RequestMapping("/saveOrUpdatePlatformSysClassify")
    @ResponseBody
    public JsonBean saveOrUpdatePlatformSysClassify(PlatformSysClassifyVO platformSysClassifyVO){
        try{
            return new JsonBean(this.platformSysClassifyService.saveOrUpdatePlatformSysClassify(platformSysClassifyVO));
        }catch (PlatformException e){
            logger.debug(e.getMessage(),e);
            return new JsonBean(e.getMessage(),-1);
        }
    }
}