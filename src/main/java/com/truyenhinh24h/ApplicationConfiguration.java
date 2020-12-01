package com.truyenhinh24h;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
	@Value("${database.name}")
	private String databaseName;

	public @Bean MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClient(), databaseName);
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
				registry.addMapping("/rest/v1/**").allowedOrigins("http://localhost:3000", "http://localhost:4000",
						"https://truyenhinh24h.live", "https://jovial-sound-296107.et.r.appspot.com", "https://lichtruyenhinh.online");
			}
		};
	}

	@Override
	protected String getDatabaseName() {
		return databaseName;
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
