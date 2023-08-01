package com.holidayDessert.utils;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

@Configuration
public class DataSourceConfig {
	
	@Bean(name = "firstDataSource")
	@Qualifier("firstDataSource")
	@ConfigurationProperties(prefix="spring.datasource.druid.first")
	public DataSource primaryDataSource() {
		return DruidDataSourceBuilder.create().build();
	}
	
	@Bean(name="jdbcTemplate")
	public JdbcTemplate mysqlJdbcTemplate (@Qualifier("firstDataSource")  DataSource dataSource ) {
		return new JdbcTemplate(dataSource);
	}
	
	@Bean(name="jdbcNameTemplate")
    public NamedParameterJdbcTemplate mysqlJdbcNameTemplate (@Qualifier("firstDataSource")  DataSource dataSource ) {
    	return new NamedParameterJdbcTemplate(dataSource);
	}
	
}
