package com.scoreanalysis.config;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @ClassName MybatisConfiguration
 * @Author yang_
 * @Description
 * @Date 2018/11/4 23:48
 */
@Configuration
@AutoConfigureAfter(DataSourceConfig.class)
@PropertySource("classpath:/mybatis.properties")
public class MybatisConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MybatisConfiguration.class);

    @Value("${mybatis.mapperLocations}")
    private String mapperLocations;
    @Value("${mybatis.typeAliasesPackage}")
    private String typeAliasesPackage;

    @Value("${mybatis.configLocation}")
    private String configLocation;

    @Autowired
    private DataSource dataSource;

    // 提供SQLSession
    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(){
        try{
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);

            // 读取配置
            sqlSessionFactoryBean.setTypeAliasesPackage(this.typeAliasesPackage);
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(this.mapperLocations);
            sqlSessionFactoryBean.setMapperLocations(resources);
            sqlSessionFactoryBean.setConfigLocation(new DefaultResourceLoader().getResource(this.configLocation));

            // 添加分页插件
            Interceptor[] plugins = new Interceptor[]{pageHelper()};
            sqlSessionFactoryBean.setPlugins(plugins);
            logger.info("Mybatis加载成功");
            return sqlSessionFactoryBean.getObject();
        }catch (IOException e){
            logger.error("mybatis resolver mapper*xml is error", e);
            return null;
        } catch (Exception e) {
            logger.error("mybatis sqlSessionFactoryBean create error", e);
            return null;
        }
    }

    @Bean(name = "pageHelper")
    PageHelper pageHelper(){
        //分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    //事务管理
    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
