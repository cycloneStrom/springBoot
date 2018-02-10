package org.poem.common.security;

/**
 * Constants for Spring Security authorities.
 * Auto 的权限名称管理
 */
public class AuthoritiesConstants {

    private AuthoritiesConstants() {
    }

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
}
