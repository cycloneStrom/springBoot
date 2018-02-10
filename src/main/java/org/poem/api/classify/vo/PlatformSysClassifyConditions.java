package org.poem.api.classify.vo;

import org.poem.core.vo.BaseVO;

/**
 * Created by poem on 2016/5/6.
 * 查询条件封装
 */
public class PlatformSysClassifyConditions  extends BaseVO{

    /**
     * 名称
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
