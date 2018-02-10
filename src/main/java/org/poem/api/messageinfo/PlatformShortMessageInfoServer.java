package org.poem.api.messageinfo;

import org.poem.api.common.vo.FaildInfoVO;
import org.poem.api.exception.PlatformException;
import org.poem.api.messageinfo.vo.PlatformShortMessageInfoVO;
import org.poem.api.messageinfo.vo.PlatformShortMsgConditons;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by poem on 2016/5/16.
 * 用户说说的管理类
 */
public interface PlatformShortMessageInfoServer {

    /**
     * 查询消息提
     * @param platformShortMsgConditons 查询条件
     * @param pageable 分页信息
     * @return 查询的结果
     */
    Page<PlatformShortMessageInfoVO> queryShortMessageByConditionOrderCreateDate(PlatformShortMsgConditons platformShortMsgConditons,Pageable pageable) throws PlatformException;

    /**
     *  保存数据
     * @param platformShortMessageInfoVO 消息提
     * @return 错误的信息
     * @throws PlatformException
     */
    List<FaildInfoVO> saveShortMessage(HttpServletRequest request,PlatformShortMessageInfoVO platformShortMessageInfoVO) throws  PlatformException;


    /**
     * 赞
     * @param platformShortMessageInfoId 赞的说说消息
     * @param platformSysUserId 档期登录人
     * @return
     * @throws PlatformException
     */
    public FaildInfoVO enjoyPlatformShowMsg(String platformShortMessageInfoId,String platformSysUserId) throws PlatformException;

    /**
     * 收藏
     * @param platformShortMessageInfoId 赞的说说消息
     * @param platformSysUserId 档期登录人
     * @return
     * @throws PlatformException
     */
    public FaildInfoVO favoritePlatformShortMsg(String platformShortMessageInfoId,String platformSysUserId) throws PlatformException;

    /**
     *  查看
     * @param platformShortMessageInfoId 赞的说说消息
     * @param platformSysUserId 档期登录人
     * @return
     * @throws PlatformException
     */
    public FaildInfoVO browseCountPlatformShortMsg(String platformShortMessageInfoId,String platformSysUserId) throws PlatformException;
}
