package com.truyenhinh24h;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class ApplicationConfiguration {

	/*
	 * Use the standard Mongo driver API to create a com.mongodb.client.MongoClient
	 * instance.
	 */
	@Value("${spring.data.mongodb.uri}")
	private String mongoUrl;
	
	public @Bean MongoClient mongoClient() {
		return MongoClients.create(mongoUrl);
	}

	public @Bean MongoTemplate mongoTemplate() {
		return new MongoTemplate(mongoClient(), "truyenhinh24h");
	}
}
