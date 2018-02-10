package org.poem;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.poem.common.config.CommunityConfig;
import org.poem.common.config.Constants;
import org.poem.common.security.WebSecurityConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;

/**
 * Created by lyw on 2016/4/13.
 * SpringBoot
 * SpringBoot自动注解扫描是在与启动文件相同的目录下 如果不在同意目录下，就扫描不到
 */

/* 这是个SpringBoot 程序 SpringBootApplication*/
@org.springframework.boot.autoconfigure.SpringBootApplication
/*自动配置*/
@EnableAutoConfiguration(exclude = {WebSecurityConfigurer.class,MetricFilterAutoConfiguration.class, MetricRepositoryAutoConfiguration.class})
/*自己注册属性，可以在整个环境那个中使用*/
@EnableConfigurationProperties({CommunityConfig.class})
/*扫描*/
@ComponentScan
/*@EnableJpaRepositories注解用于Srping JPA的代码配置*/
@EnableJpaRepositories
public class SpringBootTestApplication {


    @Value("${druid.datasource.url}")
    private String dbUrl;
    @Value("${druid.datasource.username}")
    private String username;
    @Value("${druid.datasource.password}")
    private String password;
    @Value("${druid.datasource.driverClassName}")
    private String driverClassName;
    @Value("${druid.datasource.initialSize}")
    private int initialSize;
    @Value("${druid.datasource.minIdle}")
    private int minIdle;
    @Value("${druid.datasource.maxActive}")
    private int maxActive;
    @Value("${druid.datasource.maxWait}")
    private int maxWait;
    @Value("${druid.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${druid.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
    @Value("${druid.datasource.validationQuery}")
    private String validationQuery;
    @Value("${druid.datasource.testWhileIdle}")
    private boolean testWhileIdle;
    @Value("${druid.datasource.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${druid.datasource.testOnReturn}")
    private boolean testOnReturn;
    @Value("${druid.datasource.poolPreparedStatements}")
    private boolean poolPreparedStatements;
    @Value("${druid.datasource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;
    @Value("${druid.datasource.filters}")
    private String filters;
    @Value("${druid.datasource.connectionProperties}")
    private String connectionProperties;
    @Value("${druid.datasource.useGlobalDataSourceStat}")
    private boolean useGlobalDataSourceStat;

    @Bean(initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties(prefix = "druid.datasource")
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);

        //configuration
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        datasource.setUseGlobalDataSourceStat(useGlobalDataSourceStat);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            System.err.println("druid configuration initialization filter: "+ e);
        }
        datasource.setConnectionProperties(connectionProperties);
        return new DruidDataSource();
    }

    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        return registration;
    }

    /**
     * 日志管理
     */
    private static  Logger logger = LoggerFactory.getLogger( SpringBootTestApplication.class);


    /**
     * 启动环境配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("10000MB");
        factory.setMaxRequestSize("10000MB");
        return factory.createMultipartConfig();
    }


    public static void main(String[] args) throws UnknownHostException {
        System.setProperty("org.eclipse.jetty.server.Request.maxFormContentSize", String.valueOf(Integer.MAX_VALUE));
        System.setProperty("org.eclipse.jetty.server.Request.maxFormKeys",String.valueOf(Integer.MAX_VALUE));

        SpringApplication app = new SpringApplication( SpringBootTestApplication.class);
        app.setShowBanner(true);
        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
        addDefaultProfile(app, source);
        Environment env = app.run(args).getEnvironment();
        logger.info("Access URLs:\n----------------------------------------------------------\n\t" +
                        "Local: \t\thttp://127.0.0.1:{}\n\t" +
                        "External: \thttp://{}:{}\n----------------------------------------------------------",
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port")
        );
    }

    /**
     * If no profile has been configured, set by default the "dev" profile.
     */
    private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
        if (!source.containsProperty("spring.profiles.active") &&
                !System.getenv().containsKey("SPRING_PROFILES_ACTIVE")) {
            app.setAdditionalProfiles(Constants.SPRING_PROFILE_DEVELOPMENT);
        }
    }
}
