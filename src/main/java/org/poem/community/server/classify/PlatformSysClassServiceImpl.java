package org.poem.community.server.classify;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.poem.api.classify.PlatformSysClassifyService;
import org.poem.api.classify.vo.PlatformSysClassifyConditions;
import org.poem.api.classify.vo.PlatformSysClassifyVO;
import org.poem.api.common.vo.FaildInfoVO;
import org.poem.api.exception.PlatformException;
import org.poem.community.dao.file.FileDataSourceDao;
import org.poem.community.dao.messageinfo.PlatformShortMessageInfoDao;
import org.poem.community.dao.messageinfo.PlatformShortMessageInfoFilesDao;
import org.poem.community.entity.FileDataSource;
import org.poem.community.entity.PlatformMessageInfo;
import org.poem.community.entity.PlatformMessageInfoFiles;
import org.poem.core.utils.BeanUtils;
import org.poem.core.page.CustomPageImpl;
import org.poem.community.dao.classify.PlatformSysClassDao;
import org.poem.community.entity.PlatformSysClassify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

/**
 * Created by poem on 2016/5/6.
 * 类型的服务类
 */
@Service("platformSysClassifyService")
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class PlatformSysClassServiceImpl implements PlatformSysClassifyService {

    /**
     * 日志管理
     */
    private Logger logger = LoggerFactory.getLogger(PlatformSysClassServiceImpl.class);

    /**
     * 类型DTO管理
     */
    @Autowired
    private PlatformSysClassDao platformSysClassDao;

    @Autowired
    private PlatformShortMessageInfoDao messageInfoDao;

    @Autowired
    private PlatformShortMessageInfoFilesDao messageInfoFilesDao;

    @Autowired
    private FileDataSourceDao dataSourceDao;
    /**
     * 查询记录
     * @param pageable 分页
     * @param platformSysClassifyConditions 查询条件
     * @return
     */
    @Override
    public Page<PlatformSysClassifyVO> queryPlatformSysClassifyByCondition(Pageable pageable, PlatformSysClassifyConditions platformSysClassifyConditions) throws PlatformException {
        Page<PlatformSysClassify> platformSysClassifyPage = this.platformSysClassDao.findAll(new Specification<PlatformSysClassify>() {
            @Override
            public Predicate toPredicate(Root<PlatformSysClassify> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //查询信息
                List<Predicate> predicateList = Lists.newArrayList();
                String name = platformSysClassifyConditions.getName();
                if(StringUtils.isNoneBlank(name)){
                    predicateList.add(criteriaBuilder.like(root.get("name"),name));
                }
                criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
                List<Order> orders = Lists.newArrayList();
                //排序的组装条件
                orders.add(criteriaBuilder.desc(root.get("createDate")));
                criteriaQuery.orderBy(orders);
                return null;
            }
        },pageable);
        List<PlatformSysClassifyVO> platformSysClassifyVOs = Lists.newArrayList();
        if(platformSysClassifyPage != null){
            platformSysClassifyVOs = convtPlatformClassifyToVO(platformSysClassifyPage);
            return new CustomPageImpl<>(platformSysClassifyVOs,pageable,platformSysClassifyPage.getTotalElements());
        }else{
            return new CustomPageImpl<>(platformSysClassifyVOs,pageable,0);
        }
    }

    /**
     * 把Entity转化为VO
     * @param platformSysClassifyPage 查询出来的数据
     * @return
     */
    private List<PlatformSysClassifyVO> convtPlatformClassifyToVO(Page<PlatformSysClassify> platformSysClassifyPage) throws PlatformException{
        List<PlatformSysClassifyVO> platformSysClassifyVOList = Lists.newArrayList();
        if(platformSysClassifyPage != null && CollectionUtils.isNotEmpty(platformSysClassifyPage.getContent())){
            for (PlatformSysClassify platformSysClassify : platformSysClassifyPage.getContent()) {
                PlatformSysClassifyVO platformSysClassifyVO = new PlatformSysClassifyVO();
                BeanUtils.copyProperties(platformSysClassify,platformSysClassifyVO);
                platformSysClassifyVOList.add(platformSysClassifyVO);
            }
        }
        return platformSysClassifyVOList;
    }

    /**
     * 保存数据
     * @param platformSysClassifyVO 返回数据
     * @return
     */
    @Override
    public List<FaildInfoVO> saveOrUpdatePlatformSysClassify(PlatformSysClassifyVO platformSysClassifyVO) {
        List<FaildInfoVO> failResonVOList = Lists.newArrayList();
        String classId = platformSysClassifyVO.getId();
        PlatformSysClassify platformSysClassify = new PlatformSysClassify();
        if(StringUtils.isNoneBlank(classId)){
            platformSysClassify = this.platformSysClassDao.findOne(classId);
        }
        /*查询是否存在相同的名字*/
        List<PlatformSysClassify> sameNameClassifyLists = platformSysClassDao.findClassifyNameByNameAndId(platformSysClassifyVO.getName(),platformSysClassifyVO.getId());
        if(CollectionUtils.isNotEmpty(sameNameClassifyLists)){
            failResonVOList.add(new FaildInfoVO(classId,platformSysClassifyVO.getName() + "已存在！"));
            return failResonVOList;
        }
        /*拷贝值*/
        BeanUtils.copyProperties(platformSysClassifyVO,platformSysClassify);
        platformSysClassify.setCreateDate(new Date());
        /*保存数据*/
        this.platformSysClassDao.saveAndFlush(platformSysClassify);
        return failResonVOList;
    }

    @Override
    public void deleteMsg(String infoId) throws PlatformException {
        if(StringUtils.isNotBlank(infoId)){
            //消息体
            PlatformMessageInfo messageInfo =  messageInfoDao.findOne(infoId);
            if(null!=messageInfo){
                //文件
                List<PlatformMessageInfoFiles> infoFilesList =  messageInfoFilesDao.queryFileByMsgId(messageInfo.getId());
                if(CollectionUtils.isNotEmpty(infoFilesList)){
                    List<String> fileIdList = Lists.newArrayList();
                    for (PlatformMessageInfoFiles files : infoFilesList) {
                        fileIdList.add(files.getFileInfo());
                    }
                    List<FileDataSource> dataSourceList = dataSourceDao.queryBySourceIds(fileIdList);
                    if(CollectionUtils.isNotEmpty(dataSourceList)){
                        dataSourceDao.deleteInBatch(dataSourceList);
                    }
                    messageInfoFilesDao.deleteInBatch(infoFilesList);
                }
                messageInfoDao.delete(messageInfo);
            }
        }
    }
}
