package org.poem.api.messageinfo.vo;

import org.poem.core.vo.BaseVO;

import java.util.List;

/**
 * Created by poem on 2016/5/16.
 * 查询条件
 */
public class PlatformShortMsgConditons extends BaseVO {


    /**
     * 查询我自己的说说的查询条件  -- 都是当前登录人
     */
    private String userId;

    /**
     * 查询我赞的消息的时候查询 -- 当前登录人
     */
    private String enjoyUserId;

    /**
     * 查询我的收藏的查询条件 -- 当前登录人
     */
    private String favoritUserId;

    /**
     * 首页查看我的朋友的消息 -- 所有的人
     */
    private List<String> myFriendLists;

    public String getEnjoyUserId() {
        return enjoyUserId;
    }

    public void setEnjoyUserId(String enjoyUserId) {
        this.enjoyUserId = enjoyUserId;
    }

    public String getFavoritUserId() {
        return favoritUserId;
    }

    public void setFavoritUserId(String favoritUserId) {
        this.favoritUserId = favoritUserId;
    }

    public List<String> getMyFriendLists() {
        return myFriendLists;
    }

    public void setMyFriendLists(List<String> myFriendLists) {
        this.myFriendLists = myFriendLists;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
