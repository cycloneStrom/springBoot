package org.poem.community.server.messageinfo;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.poem.api.common.vo.FaildInfoVO;
import org.poem.api.exception.PlatformException;
import org.poem.api.messageinfo.PlatformShortMessageInfoServer;
import org.poem.api.messageinfo.vo.PlatformShortMessageInfoVO;
import org.poem.api.messageinfo.vo.PlatformShortMsgConditons;
import org.poem.api.messageinfo.vo.PlatformShortMsgFileInfoVO;
import org.poem.common.utils.DateUtils;
import org.poem.community.dao.messageinfo.PlatformShortMessageInfoFilesDao;
import org.poem.community.entity.PlatformMessageInfoFiles;
import org.poem.core.utils.BeanUtils;
import org.poem.core.page.CustomPageImpl;
import org.poem.community.dao.messageinfo.PlatformShortMessageInfoDao;
import org.poem.community.entity.PlatformMessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by poem on 2016/5/17.
 * 说说的配置
 */
@Service("platformShortMessageInfoServer")
public class PlatformShortMessageInfoServerImpl implements PlatformShortMessageInfoServer {

    /**
     * 日志管理
     */
    private static Logger logger = LoggerFactory.getLogger(PlatformShortMessageInfoServerImpl.class);


    /**
     * 日志管理
     */
    @Autowired
    private PlatformShortMessageInfoDao platformShortMessageInfoDTO;

    /**
     * 当前说说的文件信息
     */
    @Autowired
    private PlatformShortMessageInfoFilesDao platformShortMessageInfoFilesDTO;
    /**
     *
     * @param platformShortMsgConditons 查询条件
     * @param pageable 分页信息
     * @return
     * @throws PlatformException
     */
    @Override
    public Page<PlatformShortMessageInfoVO> queryShortMessageByConditionOrderCreateDate(PlatformShortMsgConditons platformShortMsgConditons,Pageable pageable) throws PlatformException {

        Page<PlatformMessageInfo> platformShortMessageInfos = this.platformShortMessageInfoDTO.findAll(new Specification<PlatformMessageInfo>() {
            @Override
            public Predicate toPredicate(Root<PlatformMessageInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //查询信息
                List<Predicate> predicateList = Lists.newArrayList();
                /*当前登录人的消息*/
                if(StringUtils.isNoneBlank(platformShortMsgConditons.getUserId())){
                    predicateList.add(criteriaBuilder.equal(root.get("platformSysUser").get("id"),platformShortMsgConditons.getUserId()));
                }
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("publishDate")));
                criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
                return null;
            }
        },pageable);
        List<PlatformShortMessageInfoVO> platformShortMessageInfoVOs = Lists.newArrayList();
        if(platformShortMessageInfos == null){
            return new CustomPageImpl<>(platformShortMessageInfoVOs,pageable,0);
        }
        List<PlatformMessageInfoFiles> platformMessageInfoFiles ;
        List<PlatformShortMsgFileInfoVO> fileId;
        PlatformShortMsgFileInfoVO platformShortMsgFileInfoVO;
        /*复制*/
        for(PlatformMessageInfo platformShortMessageInfo : platformShortMessageInfos.getContent()){
            PlatformShortMessageInfoVO platformShortMessageInfoVO = new PlatformShortMessageInfoVO();
            BeanUtils.copyProperties(platformShortMessageInfo,platformShortMessageInfoVO);
            platformShortMessageInfoVO.setPublishDate(DateUtils.formatLocalDateTime(platformShortMessageInfo.getPublishDate()));
            platformMessageInfoFiles = this.platformShortMessageInfoFilesDTO.queryFileByMsgId(platformShortMessageInfo.getId());
            if(CollectionUtils.isNotEmpty( platformMessageInfoFiles )){
                fileId = Lists.newArrayList();
                for (PlatformMessageInfoFiles platformMessageInfoFile : platformMessageInfoFiles) {
                    platformShortMsgFileInfoVO = new PlatformShortMsgFileInfoVO();
                    platformShortMsgFileInfoVO.setFileId(platformMessageInfoFile.getFileInfo() );
                    fileId.add( platformShortMsgFileInfoVO );
                }
                platformShortMessageInfoVO.setFileId( fileId );
            }
            platformShortMessageInfoVO.setPublishTool(platformShortMessageInfo.getPublishTool());
            platformShortMessageInfoVOs.add(platformShortMessageInfoVO);
        }
        return new CustomPageImpl<>(platformShortMessageInfoVOs,pageable,platformShortMessageInfos.getTotalElements());
    }

    /**
     * 保存
     * @param platformShortMessageInfoVO 消息提
     * @return 保存信息
     * @throws PlatformException
     */
    @Override
    public List<FaildInfoVO> saveShortMessage(HttpServletRequest request,PlatformShortMessageInfoVO platformShortMessageInfoVO) throws PlatformException {
        PlatformMessageInfo platformShortMessageInfo = new PlatformMessageInfo();
        BeanUtils.copyProperties(platformShortMessageInfoVO, platformShortMessageInfo);
        platformShortMessageInfo.setPublishDate(new Date());
        platformShortMessageInfo.setBrowseCount(0l);
        platformShortMessageInfo.setEnjoyCount(0l);
        platformShortMessageInfo.setCommentCount(0l);
        platformShortMessageInfo.setPublishTool(platformShortMessageInfoVO.getPublishTool());
        platformShortMessageInfo = this.platformShortMessageInfoDTO.save(platformShortMessageInfo);

        /*保存文件信息*/
        if(CollectionUtils.isNotEmpty(platformShortMessageInfoVO.getFileId())){
            List<PlatformMessageInfoFiles> platformShortMessageInfoFileses = Lists.newArrayList();
            for (PlatformShortMsgFileInfoVO platformShortMsgFileInfoVO : platformShortMessageInfoVO.getFileId()) {
                PlatformMessageInfoFiles platformShortMessageInfoFiles = new PlatformMessageInfoFiles();
                platformShortMessageInfoFiles.setFileName(platformShortMsgFileInfoVO.getFileName());
                platformShortMessageInfoFiles.setFileInfo(platformShortMsgFileInfoVO.getFileId());
                platformShortMessageInfoFiles.setPlatformShortMessageInfo(platformShortMessageInfo);
                platformShortMessageInfoFileses.add(platformShortMessageInfoFiles);
            }
            if(CollectionUtils.isNotEmpty(platformShortMessageInfoFileses)){
                this.platformShortMessageInfoFilesDTO.save(platformShortMessageInfoFileses);
            }
        }
        return null;
    }

    /**
     * 赞
     * @param platformShortMessageInfoId 赞的说说消息
     * @param platformSysUserId 档期登录人
     * @return
     * @throws PlatformException
     */
    @Override
    public FaildInfoVO enjoyPlatformShowMsg(String platformShortMessageInfoId,String platformSysUserId) throws PlatformException{
        /*赞的说说消息*/
        if(StringUtils.isBlank(platformShortMessageInfoId) || StringUtils.isBlank(platformSysUserId)){
            throw  new PlatformException("参数错误！");
        }
        PlatformMessageInfo platformShortMessageInfo = this.platformShortMessageInfoDTO.findOne(platformShortMessageInfoId);
        if(platformShortMessageInfo == null){
            throw  new PlatformException("查询发生错误！");
        }
        platformShortMessageInfo.setEnjoyCount(platformShortMessageInfo.getEnjoyCount() + 1);
        String enjoyShort = platformShortMessageInfo.getEnjoyUserIds();
        if(StringUtils.isBlank(enjoyShort)){
            enjoyShort = platformSysUserId;
        }else{
            enjoyShort = enjoyShort + "," + platformSysUserId;
        }
        platformShortMessageInfo.setEnjoyUserIds(enjoyShort);
        this.platformShortMessageInfoDTO.save(platformShortMessageInfo);
        return null;
    }


    /**
     * 收藏
     * @param platformShortMessageInfoId 赞的说说消息
     * @param platformSysUserId 档期登录人
     * @return
     * @throws PlatformException
     */
    public FaildInfoVO favoritePlatformShortMsg(String platformShortMessageInfoId,String platformSysUserId) throws PlatformException{
         /*赞的说说消息*/
        if(StringUtils.isBlank(platformShortMessageInfoId) || StringUtils.isBlank(platformSysUserId)){
            throw  new PlatformException("参数错误！");
        }
        PlatformMessageInfo platformShortMessageInfo = this.platformShortMessageInfoDTO.findOne(platformShortMessageInfoId);
        if(platformShortMessageInfo == null){
            throw  new PlatformException("查询发生错误！");
        }
        platformShortMessageInfo.setFavorityCount(platformShortMessageInfo.getFavorityCount() + 1);
        String enjoyShort = platformShortMessageInfo.getFavorityUserIds();
        if(StringUtils.isBlank(enjoyShort)){
            enjoyShort = platformSysUserId;
        }else{
            enjoyShort = enjoyShort + "," + platformSysUserId;
        }
        platformShortMessageInfo.setFavorityUserIds(enjoyShort);
        this.platformShortMessageInfoDTO.save(platformShortMessageInfo);
        return null;
    }

    /**
     *  查看
     * @param platformShortMessageInfoId 赞的说说消息
     * @param platformSysUserId 档期登录人
     * @return
     * @throws PlatformException
     */
    public FaildInfoVO browseCountPlatformShortMsg(String platformShortMessageInfoId,String platformSysUserId) throws PlatformException{
        /*赞的说说消息*/
        if(StringUtils.isBlank(platformShortMessageInfoId) || StringUtils.isBlank(platformSysUserId)){
            throw  new PlatformException("参数错误！");
        }
        PlatformMessageInfo platformShortMessageInfo = this.platformShortMessageInfoDTO.findOne(platformShortMessageInfoId);
        if(platformShortMessageInfo == null){
            throw  new PlatformException("查询发生错误！");
        }
        platformShortMessageInfo.setBrowseCount(platformShortMessageInfo.getBrowseCount() + 1);
        String enjoyShort = platformShortMessageInfo.getBrowseUserIds();
        if(StringUtils.isBlank(enjoyShort)){
            enjoyShort = platformSysUserId;
        }else{
            enjoyShort = enjoyShort + "," + platformSysUserId;
        }
        platformShortMessageInfo.setEnjoyUserIds(enjoyShort);
        this.platformShortMessageInfoDTO.save(platformShortMessageInfo);
        return null;
    }
}
