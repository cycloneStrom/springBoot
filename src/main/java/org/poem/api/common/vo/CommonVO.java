package org.poem.api.common.vo;

import org.poem.core.vo.BaseVO;

/**
 * Created by poem on 2016/5/6.
 * 返回的基础信息
 */
public class CommonVO extends BaseVO {

    /**
     * 返回信息
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
