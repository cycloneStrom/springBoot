package org.poem.common.security;

import org.poem.community.entity.PlatformSecurityAccount;
import org.poem.core.dao.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Created by poem on 2016/6/18.
 * 查询用户的账号信息
 */
public interface PlatformSecurityAccountRepository extends BaseRepository<PlatformSecurityAccount, Long> {

    /**
     * @Title: findOneByAccount
     * @Description: 获取激活的用户账号信息
     * @author guohongjin
     * @date 2015-08-24
     * @throw YnCorpSysException
     */

    @Query("SELECT psa FROM PlatformSecurityAccount psa WHERE psa.account=?1 AND psa.status=?2")
    Optional<PlatformSecurityAccount> findPlatformSecurityAccountByAccount(String Account, Byte status);
}
