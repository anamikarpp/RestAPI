package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.pivotal.cfenv.core.CfCredentials;
import io.pivotal.cfenv.core.CfEnv;

@Configuration
@Profile("cloud")
public class CloudDataSourceConfig {

	@Bean
	public CfEnv cfenv() {
		return new CfEnv();
	}

	@Bean
	public DataSource dataSource(CfEnv cfenv) {
		HikariConfig config = new HikariConfig();
		CfCredentials credentials = cfenv.findCredentialsByTag("PostgreSQL");
		config.setJdbcUrl("jdbc:postgresql://" + credentials.getHost() + ":" + credentials.getPort() + "/"
				+ credentials.getString("db_name"));
		config.setUsername(credentials.getUsername());
		config.setPassword(credentials.getPassword());
		return new HikariDataSource(config);

	}

}
