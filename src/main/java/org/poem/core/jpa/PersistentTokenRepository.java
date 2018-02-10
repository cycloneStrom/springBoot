package org.poem.core.jpa;

import org.joda.time.LocalDate;
import org.poem.community.entity.PersistentToken;
import org.poem.community.entity.PlatformSecurityAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by poem on 2016/6/18.
 */
public interface PersistentTokenRepository extends JpaRepository<PersistentToken, String> {

    List<PersistentToken> findByPlatformSecurityAccount(PlatformSecurityAccount platformSecurityAccount);

    List<PersistentToken> findByTokenDateBefore(LocalDate localDate);

}
