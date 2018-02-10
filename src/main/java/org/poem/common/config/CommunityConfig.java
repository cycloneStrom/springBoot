package org.poem.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

/**
 * Created by poem on 2016/5/29.
 * 聊天配置
 */
@ConfigurationProperties(locations = "classpath:config/community.properties",prefix ="community")
public class CommunityConfig {

    /**
     * 管理账户
     */
    @NotNull
    private String admin;

    /**
     * 密码
     */
    @NotNull
    private String password;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
