package com.truyenhinh24h;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


@EnableCaching
@Configuration
public class ApplicationConfiguration extends AbstractMongoClientConfiguration {

	/*
	 * Use the standard Mongo driver API to create a com.mongodb.client.MongoClient
	 * instance.
	 */
	@Value("${spring.data.mongodb.uri}")
	private String mongoUrl;
	
	private final String dataBaseName = "truyenhinh24h_test";

//	public @Bean MongoClient mongoClient() {
//		return MongoClients.create(mongoUrl);
//	}

	public @Bean MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClient(), dataBaseName);
	}

	@Bean
	MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
		return new MongoTransactionManager(dbFactory);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}
		};
	}

	@Override
	protected String getDatabaseName() {
		return dataBaseName;
	}

	@Override
	public MongoClient mongoClient() {
		return MongoClients.create(mongoUrl);
	}

	@Override
	protected boolean autoIndexCreation() {
		return true;
	}
	
}
