package org.poem.common.config;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * Created by poem on 2016/6/24.
 * 数据库配置
 */
@Configuration
@EnableJpaRepositories("org.poem")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
public class DatabaseConfiguration implements EnvironmentAware {

    /**
     * 日志管理
     */
    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    /**
     * 属性设置
     */
    private RelaxedPropertyResolver dataSourcePropertyResolver;

    /**
     * 环境变量
     */
    private Environment env;

    /**
     * 直方图
     */
    @Autowired(required = false)
    private MetricRegistry metricRegistry;

    /**
     * 环境变量
     * @param env
     */
    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
        this.dataSourcePropertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
    }

    /**
     * 数据库配置
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    @Profile("!" + Constants.SPRING_PROFILE_CLOUD)
    public DataSource dataSource() {
        log.debug("Configuring Datasource");
        if (dataSourcePropertyResolver.getProperty("url") == null && dataSourcePropertyResolver.getProperty("databaseName") == null) {
            log.error("Your database connection pool configuration is incorrect! The application cannot start. Please check your Spring profile, current profiles are: {}",
                    Arrays.toString(env.getActiveProfiles()));

            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(dataSourcePropertyResolver.getProperty("dataSourceClassName"));
        if (StringUtils.isEmpty(dataSourcePropertyResolver.getProperty("url"))) {
            config.addDataSourceProperty("databaseName", dataSourcePropertyResolver.getProperty("databaseName"));
            config.addDataSourceProperty("serverName", dataSourcePropertyResolver.getProperty("serverName"));
        } else {
            config.addDataSourceProperty("url", dataSourcePropertyResolver.getProperty("url"));
        }
        config.addDataSourceProperty("user", dataSourcePropertyResolver.getProperty("username"));
        config.addDataSourceProperty("password", dataSourcePropertyResolver.getProperty("password"));
        config.setMaximumPoolSize(Integer.parseInt(dataSourcePropertyResolver.getProperty("maximumPoolSize")));

        //MySQL optimizations, see https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
        if ("com.mysql.jdbc.jdbc2.optional.MysqlDataSource".equals(dataSourcePropertyResolver.getProperty("dataSourceClassName"))) {
            config.addDataSourceProperty("cachePrepStmts", dataSourcePropertyResolver.getProperty("cachePrepStmts", "true"));
            config.addDataSourceProperty("prepStmtCacheSize", dataSourcePropertyResolver.getProperty("prepStmtCacheSize", "250"));
            config.addDataSourceProperty("prepStmtCacheSqlLimit", dataSourcePropertyResolver.getProperty("prepStmtCacheSqlLimit", "2048"));

        }
        if (metricRegistry != null) {
            config.setMetricRegistry(metricRegistry);
        }
        return new HikariDataSource(config);
    }

    @Bean
    public Hibernate4Module hibernate4Module() {
        return new Hibernate4Module();
    }
}
