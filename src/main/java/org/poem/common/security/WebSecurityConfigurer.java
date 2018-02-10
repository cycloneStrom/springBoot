package org.poem.common.security;

import org.poem.common.config.Constants;
import org.poem.common.filter.AuthorizationActiveFilter;
import org.poem.common.filter.AuthorizationExpiredFilter;
import org.poem.common.filter.CsrfCookieGeneratorFilter;
import org.poem.common.handler.AjaxAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.csrf.CsrfFilter;

import javax.annotation.Resource;
import javax.servlet.Filter;

/**
 * Created by poem on 2016/5/29.
 * Spring security
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Profile("!" + Constants.SPRING_PROFILE_FAST)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private static String rememberMeKey = "remember-me";
    /**
     * 全局变量 和System.setProperty()有一拼
     */
    @Autowired
    private Environment env;
    /**
     * 登录配置
     */
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private SourceSecurityAuthenticationEntry sourceAuthenticationEntry;

    /**
     *记住我
     */
    @Resource
    private RememberMeServices rememberMeServices;

    /**
     * 对整个应用的框架的全局权限的处理类
     */
    @Autowired
    private AuthenticationProvider authenticationProvider;

    /**
     * 登录成功的处理类
     */
    @Autowired
    private LoginSuccessAuthenticationHandler authenticationSuccessHandler;
    /**
     * 退出登录的时候的处理类
     */
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    /**
     * 获取全局的权限管理类
     * 登录从这儿开始 TokenAuthenticationProvider  这儿登录查询
     * @param auth 权限认证机制
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    /**
     * http config
     * @param http http request
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception{
        http
                .httpBasic().authenticationEntryPoint(sourceAuthenticationEntry)
                .and()
                    .csrf()
                    .ignoringAntMatchers("/login")
                    .ignoringAntMatchers("/api/login/loginForm")
                    .ignoringAntMatchers("/api/login/regiestAccount")
                    .ignoringAntMatchers( "/fileManage/downStream" )//文件流下载
                .and()
                    .addFilterBefore(authorizationActiveFilter(), AuthenticationFilter.class)
                    .addFilterAfter(new CsrfCookieGeneratorFilter(), CsrfFilter.class)
                    .addFilterAfter(authorizationExpiredFilter(), AuthenticationFilter.class)
                    .formLogin()
                    .loginPage("/login.htm") /*登录的页面或者是请求*/
                    .loginProcessingUrl("/api/login/loginForm")
                    .successHandler(authenticationSuccessHandler)
                    .usernameParameter("username")
                    .passwordParameter("password")
                     /*这儿不做登录成功后的页面的自动重定向处理，页面的改变在前端使用angularJs处理*/
//                    .defaultSuccessUrl("/pages/platform/index.html")
                    .permitAll()
                .and()
                    .logout()
                    .logoutUrl("/api/logout")
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessHandler(logoutSuccessHandler)
                    .permitAll()
                .and()
                    .rememberMe()
                    .rememberMeServices(rememberMeServices)
                    .key(rememberMeKey)
                .and()
                    .authorizeRequests().anyRequest().authenticated() /**以下的请求需要权限验证*/
                    .antMatchers("/api/login/regiestAccount").permitAll()
                    .antMatchers("/activeSystem").permitAll()
                    .antMatchers("/api/login/loginForm").permitAll()
                    .antMatchers("/error").permitAll()
                    .antMatchers("/chatMessage").permitAll()
                    .antMatchers("/sockjs/chatMessage" ).permitAll()
                    .antMatchers("/api/logs/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/classifyController/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/commonController/**").hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER)
                    .antMatchers("/business/**").hasAnyAuthority(AuthoritiesConstants.ADMIN,AuthoritiesConstants.USER)
                .and()
                    .headers()
                    .frameOptions()
                    .disable();
    }

    /**
     * web
     * @param webSecurity
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity webSecurity) throws Exception{
        webSecurity
                .ignoring()
                .antMatchers("/assets/**")
                .antMatchers("/components/**")
                .antMatchers("/static/**")
                .antMatchers("/bower_components/**")
                .antMatchers("/pages/**")
                .antMatchers( "/fileManger/**" );//文件上传的不拦截
    }
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new AjaxAuthenticationFailureHandler("/activeSystem");
    }
    @Bean
    public Filter authorizationActiveFilter() {
        return new AuthorizationActiveFilter(authenticationFailureHandler());
    }

    @Bean
    public Filter authorizationExpiredFilter() {
        return new AuthorizationExpiredFilter(authenticationFailureHandler());
    }

}
