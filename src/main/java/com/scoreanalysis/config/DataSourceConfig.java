package com.scoreanalysis.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @ClassName DataSourceConfig
 * @Author yang_
 * @Description
 * @Date 2018/11/4 23:21
 */
@Component
@Configuration
@PropertySource("classpath:/db.properties")
public class DataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.type}")
    private String type;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.initSize}")
    private int initialSize;

    @Value("${spring.datasource.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.filters}")
    private String filters;

    @Bean(name = "dataSource", destroyMethod = "close", initMethod = "init")
    @Primary
    public DataSource dataSource(){
        logger.info("开始构建"+this.url);
        DruidDataSource dataSource = new DruidDataSource();
        try{
            dataSource.setUrl(this.url);
            dataSource.setDbType(this.type);
            dataSource.setUsername(this.username);
            dataSource.setPassword(this.password);
            dataSource.setDriverClassName(this.driverClassName);
            dataSource.setInitialSize(this.initialSize);
            dataSource.setMinIdle(this.minIdle);
            dataSource.setMaxActive(this.maxActive);
            dataSource.setMaxWait(this.maxWait);
            dataSource.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
            dataSource.setMinEvictableIdleTimeMillis(this.minEvictableIdleTimeMillis);
            dataSource.setValidationQuery(this.validationQuery);
            dataSource.setTestWhileIdle(this.testWhileIdle);
            dataSource.setTestOnBorrow(this.testOnBorrow);
            dataSource.setTestOnReturn(this.testOnReturn);
            dataSource.setPoolPreparedStatements(this.poolPreparedStatements);
            dataSource.setFilters(this.filters);
        }catch (SQLException e){
            logger.error("druid configuration initialization filter",e);
        }

        return dataSource;
    }


}
