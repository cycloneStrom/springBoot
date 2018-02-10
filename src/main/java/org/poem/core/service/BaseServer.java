package org.poem.core.service;

import org.poem.core.entity.BaseId;
import org.poem.core.vo.BaseVO;

/**
 * Created by poem on 2016/4/15.
 * 基础服务  Server
 */
public abstract class BaseServer<T extends BaseId<String>, K extends BaseVO> extends AbstractBaseService<String,T, K> {
}
