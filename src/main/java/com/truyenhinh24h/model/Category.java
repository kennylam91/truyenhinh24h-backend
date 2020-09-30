package com.truyenhinh24h.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "categories")
@Data
public class Category {
	
	@Transient
	public static final String SEQUENCE_NAME = "categories_sequence";
	
	@Id
	private Long categoryId;
	
	private String name;

}
