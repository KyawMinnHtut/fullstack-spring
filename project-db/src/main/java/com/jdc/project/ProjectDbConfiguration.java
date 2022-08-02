package com.jdc.project;

import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@Configuration
@PropertySource({
	"classpath:db-config.properties",
	"classpath:messages.properties"
})
public class ProjectDbConfiguration {

	@Bean
	MariaDbDataSource dataSource(
			@Value("${db.url}") String url, 
			@Value("${db.username}") String user, 
			@Value("${db.password}") String password) throws SQLException {
		
		var ds = new MariaDbDataSource();
		ds.setUrl(url);
		ds.setUser(user);
		ds.setPassword(password);
		return ds;
	}
	
	@Bean
	@Scope("prototype")
	NamedParameterJdbcTemplate template(MariaDbDataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Bean
	SimpleJdbcInsert memberInsert(MariaDbDataSource dataSource) {
		var insert = new SimpleJdbcInsert(dataSource);
		insert.setTableName("member");
		insert.setGeneratedKeyName("id");
		return insert;
	}

	@Bean
	SimpleJdbcInsert projectInsert(MariaDbDataSource dataSource) {
		var insert = new SimpleJdbcInsert(dataSource);
		insert.setTableName("project");
		insert.setGeneratedKeyName("id");
		return insert;
	}

	@Bean
	SimpleJdbcInsert taskInsert(MariaDbDataSource dataSource) {
		var insert = new SimpleJdbcInsert(dataSource);
		insert.setTableName("task");
		insert.setGeneratedKeyName("id");
		return insert;
	}
	
	@Bean
	SimpleJdbcInsert assignmentInsert(MariaDbDataSource dataSource) {
		var insert = new SimpleJdbcInsert(dataSource);
		insert.setTableName("assignment");
		insert.setGeneratedKeyName("id");
		return insert;
	}
	
}
