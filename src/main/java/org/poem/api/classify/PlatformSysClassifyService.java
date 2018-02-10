package org.poem.api.classify;


import org.poem.api.classify.vo.PlatformSysClassifyConditions;
import org.poem.api.classify.vo.PlatformSysClassifyVO;
import org.poem.api.common.vo.FaildInfoVO;

import org.poem.api.exception.PlatformException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Created by poem on 2016/5/2.
 * 基础大类的服务类
 */
public interface PlatformSysClassifyService{

    /**
     * 根据查询条件查询记录
     * @param pageable 分页
     * @param platformSysClassifyConditions 查询条件
     * @return
     */
    public  Page<PlatformSysClassifyVO> queryPlatformSysClassifyByCondition(Pageable pageable, PlatformSysClassifyConditions platformSysClassifyConditions) throws PlatformException;

    /**
     * 保存或者是跟新数据
     * @param platformSysClassifyVO 返回数据
     * @return
     */
    public List<FaildInfoVO> saveOrUpdatePlatformSysClassify(PlatformSysClassifyVO platformSysClassifyVO) throws PlatformException;

    /**
     * 删除消息
     * @param infoId
     * @return
     * @throws PlatformException
     */
    public void deleteMsg(String infoId) throws PlatformException;
}
