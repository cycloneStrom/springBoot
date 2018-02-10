package org.poem.api.classify.vo;

import org.poem.core.vo.BaseVO;


/**
 * Created by poem on 2016/5/6.
 * 基础大类
 */
public class PlatformSysClassifyVO extends BaseVO {
    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String describe;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
