package org.poem.common.config;

import org.poem.api.exception.PlatformException;
import org.poem.api.user.PlatformSecurityAccountService;
import org.poem.api.user.PlatformSysUserService;
import org.poem.api.user.vo.PlatformSecurityAccountVO;
import org.poem.api.user.vo.PlatformSysUserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理
 * 用户的登录，授权都在这儿进行的，注意
 * important ！！！！！！
 */
@Service("tokenAuthenticationProvider")
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(TokenAuthenticationProvider.class);

    /** 根据用户名和密码获取用户信息的服务. */
    @Autowired
    private PlatformSecurityAccountService platformSecurityAccountService;

    /**
     * 是否默认 oauth的认证。
     */
    private boolean isOAuth_Defult = false;

    /**
     * Additional authentication checks.
     *
     * @param userDetails the user details
     * @param authentication the authentication
     * @throws AuthenticationException the authentication exception
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {

    }

    /**
     * Retrieve user.
     *
     * @param username the username
     * @param authentication the authentication
     * @return the user details
     * @throws AuthenticationException the authentication exception
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
        UserDetails loadedUser = null;
        PlatformSecurityAccountVO vo = null;
        try {
            /*第三方认知*/
            if(isOAuth_Defult) {
                vo = new PlatformSecurityAccountVO();
                List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                //给用户状态管理域的权限。
                /**
                 * 权限的设定在org.poem.core.security.WebSecurityConfigurer需要使用
                 */
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_THIRD"));
                vo.setGrantedAuthorities(grantedAuthorities);
                loadedUser = vo;
                return loadedUser;
            }
            /*获取加密的用户*/
            String password = (String) authentication.getCredentials();
            vo = this.platformSecurityAccountService.loadUserByUsernameAndPwd(username,password);
        } catch (DataAccessException repositoryProblem) {
            throw new AuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        } catch (PlatformException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
        if (vo == null) {
            throw new AuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        /*给管理员授权*/
        if("1".equals(vo.getId()) && !isOAuth_Defult){
            //给用户状态管理域的权限。
            /**
             * 权限的设定在org.poem.core.security.WebSecurityConfigurer需要使用
             */
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            vo.setGrantedAuthorities(grantedAuthorities);
            logger.info("授予用户：" +  vo.getUsername() + "权限 ：" + "ROLE_USER" + " 、ROLE_ADMIN");
        }else {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            logger.info("授予用户：" +  vo.getUsername() + "权限 ：" + "ROLE_USER");
        }

        vo.setGrantedAuthorities(grantedAuthorities);
        loadedUser = vo;
        return loadedUser;
    }

    /**
     * Authenticate.
     *
     * @param authentication the authentication
     * @return the authentication
     * @throws AuthenticationException the authentication exception
     */
    @Override
    public Authentication authenticate(Authentication authentication) {
        Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication,
                messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports", "Only UsernamePasswordAuthenticationToken is supported")
        );

        // Determine username
        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
        UserDetails user = null;
        isOAuth_Defult = false;
        //encryStr
        if(username.equals("NONE_PROVIDED") && authentication.getDetails() != null) {
            this.isOAuth_Defult = true;
        }
        try {
            user = retrieveUser(username, (UsernamePasswordAuthenticationToken) authentication);
        } catch (AuthenticationException e){
            LOG.error(e.getMessage(), e);
            throw new BadCredentialsException(e.getMessage(), e.getCause());
        }
        Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");

        return createSuccessAuthentication(user, authentication, user);
    }
}
