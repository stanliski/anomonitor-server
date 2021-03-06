package com.netease.anomonitor.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.netease.anomonitor.datasource.DynamicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class DataSourceConfig {
    @Value("${spring.datasource.url}")
    private String defaultDBUrl;
    @Value("${spring.datasource.username}")
    private String defaultDBUser;
    @Value("${spring.datasource.password}")
    private String defaultDBPassword;
    @Value("${spring.datasource.driverClassName}")
    private String defaultDBDreiverName;

    @Bean
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();
        DruidDataSource defaultDataSource = new DruidDataSource();
        defaultDataSource.setUrl(defaultDBUrl);
        defaultDataSource.setUsername(defaultDBUser);
        defaultDataSource.setPassword(defaultDBPassword);
        defaultDataSource.setDriverClassName(defaultDBDreiverName);

        Map<Object, Object> map = new HashMap<>();
        map.put("default", defaultDataSource);
        dynamicDataSource.setTargetDataSources(map);
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);
        return dynamicDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("dynamicDataSource") DataSource dynamicDataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource);
        return bean.getObject();
    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory)
            throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


}
