package org.poem.common.security;


import org.joda.time.LocalDate;
import org.poem.community.entity.PersistentToken;
import org.poem.core.jpa.PersistentTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Custom implementation of Spring Security's RememberMeServices.
 * <p>
 * Persistent tokens are used by Spring Security to automatically log in users.
 * <p>
 * This is a specific implementation of Spring Security's remember-me authentication, but it is much
 * more powerful than the standard implementations:
 * <ul>
 * <li>It allows a user to see the list of his currently opened sessions, and invalidate them</li>
 * <li>It stores more information, such as the IP address and the user agent, for audit purposes<li>
 * <li>When a user logs out, only his current session is invalidated, and not all of his sessions</li>
 * </ul>
 * <p>
 * This is inspired by:
 * <ul>
 * <li><a href="http://jaspan.com/improved_persistent_login_cookie_best_practice">Improved Persistent Login Cookie
 * Best Practice</a></li>
 * <li><a href="https://github.com/blog/1661-modeling-your-app-s-user-session">Github's "Modeling your App's ProxyUser Session"</a></li></li>
 * </ul>
 * <p>
 * The main algorithm comes from Spring Security's PersistentTokenBasedRememberMeServices, but this class
 * couldn't be cleanly extended.
 * <p>
 *
 *     remember me service
 */
@Service("customPersistentRememberMeServices")
public class CustomPersistentRememberMeServices extends AbstractRememberMeServices {

    private final Logger log = LoggerFactory.getLogger(CustomPersistentRememberMeServices.class);

    // Token is valid for one month
    private static final int TOKEN_VALIDITY_DAYS = 31;

    private static final int TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * TOKEN_VALIDITY_DAYS;

    private static final int DEFAULT_SERIES_LENGTH = 16;

    private static final int DEFAULT_TOKEN_LENGTH = 16;

    private SecureRandom random;

    /**
     * 登录token管理
     */
    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    /**
     * 登录权限管理
     */
    @Autowired
    private PlatformSecurityAccountRepository platformSecurityAccountRepository;

    @Autowired
    public CustomPersistentRememberMeServices(Environment env, org.springframework.security.core.userdetails.UserDetailsService userDetailsService) {
        super(env.getProperty("jhipster.security.rememberme.key"), userDetailsService);
        random = new SecureRandom();
    }

    /**
     * 登录成功的操作
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication
     */
    @Override
    protected void onLoginSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        String login = authentication.getName();

        log.debug("Creating new persistent login for user {}", login);
        PersistentToken token = platformSecurityAccountRepository.findPlatformSecurityAccountByAccount(login,(byte)1).map(u -> {
            PersistentToken persistentToken = new PersistentToken();
            persistentToken.setId(generateSeriesData());
            persistentToken.setPlatformSecurityAccount(u);
            persistentToken.setTokenValue(generateTokenData());
            persistentToken.setTokenDate(new LocalDate());
            persistentToken.setIpAddress(httpServletRequest.getRemoteAddr());
            persistentToken.setUserAgent(httpServletRequest.getHeader("ProxyUser-Agent"));
            return persistentToken;
        }).orElseThrow(() -> new UsernameNotFoundException("ProxyUser " + login + " was not found in the database"));
        try {
            persistentTokenRepository.saveAndFlush(token);
            addCookie(token, httpServletRequest, httpServletResponse);
        } catch (DataAccessException e) {
            log.error("Failed to save persistent token ", e);
        }
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] strings, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws RememberMeAuthenticationException, UsernameNotFoundException {

        PersistentToken token = getPersistentToken(strings);
        String login = token.getPlatformSecurityAccount().getAccount();

        // Token also matches, so login is valid. Update the token value, keeping the *same* series number.
        log.debug("Refreshing persistent login token for user '{}', series '{}'", login, token.getId());
        token.setTokenDate(new LocalDate());
        token.setTokenValue(generateTokenData());
        token.setIpAddress(httpServletRequest.getRemoteAddr());
        token.setUserAgent(httpServletRequest.getHeader("ProxyUser-Agent"));
        try {
            persistentTokenRepository.saveAndFlush(token);
            addCookie(token, httpServletRequest, httpServletResponse);
        } catch (DataAccessException e) {
            log.error("Failed to update token: ", e);
            throw new RememberMeAuthenticationException("Autologin failed due to data access problem", e);
        }
        return getUserDetailsService().loadUserByUsername(strings[0]);
    }


    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String rememberMeCookie = extractRememberMeCookie(request);
        if (rememberMeCookie != null && rememberMeCookie.length() != 0) {
            try {
                String[] cookieTokens = decodeCookie(rememberMeCookie);
                log.info(cookieTokens.toString());
            } catch (InvalidCookieException ice) {
                log.info("Invalid cookie, no persistent token could be deleted");
            } catch (RememberMeAuthenticationException rmae) {
                log.debug("No persistent token found, so no token could be deleted");
            }
        }
        super.logout(request, response, authentication);
    }


    private PersistentToken getPersistentToken(String[] cookieTokens) {
        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain " + 2 +
                    " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }
        String presentedSeries = cookieTokens[0];
        String presentedToken = cookieTokens[1];
        PersistentToken token = persistentTokenRepository.findOne(presentedSeries);

        if (token == null) {
            // No series match, so we can't authenticate using this cookie
            throw new RememberMeAuthenticationException("No persistent token found for series id: " + presentedSeries);
        }

        // We have a match for this user/series combination
        log.info("presentedToken={} / tokenValue={}", presentedToken, token.getTokenValue());
        if (!presentedToken.equals(token.getTokenValue())) {
            // Token doesn't match series value. Delete this session and throw an exception.
            persistentTokenRepository.delete(token);
            throw new RememberMeAuthenticationException("Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack.");
        }

        if (token.getTokenDate().plusDays(TOKEN_VALIDITY_DAYS).isBefore(LocalDate.now())) {
            persistentTokenRepository.delete(token);
            throw new RememberMeAuthenticationException("Remember-me login has expired");
        }
        return token;
    }

    private String generateSeriesData() {
        byte[] newSeries = new byte[DEFAULT_SERIES_LENGTH];
        random.nextBytes(newSeries);
        return new String(Base64.encode(newSeries));
    }

    private String generateTokenData() {
        byte[] newToken = new byte[DEFAULT_TOKEN_LENGTH];
        random.nextBytes(newToken);
        return new String(Base64.encode(newToken));
    }

    private void addCookie(PersistentToken token, HttpServletRequest request, HttpServletResponse response) {
        setCookie(
                new String[]{token.getId(), token.getTokenValue()},
                TOKEN_VALIDITY_SECONDS, request, response);
    }
}
