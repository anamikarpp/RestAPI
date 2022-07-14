package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.pivotal.cfenv.core.CfEnv;


@Configuration
@Profile("default")
public class LocalDataSourceConfig {

	@Bean
	public CfEnv cfenv() {
		return new CfEnv();
	}

	@Bean
	public DataSource dataSource(CfEnv cfenv) {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:postgresql://localhost:5432/employees"); 
		config.setUsername("postgres");
		config.setPassword("anamika");
		return new HikariDataSource(config);

	}

}
