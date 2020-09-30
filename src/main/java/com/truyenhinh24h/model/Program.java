package com.truyenhinh24h.model;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "programs")
@Data
public class Program {
	
	@Transient
	public static final String SEQUENCE_NAME = "program_sequence";
	
	@Id
	private Long programId;
	
	private String name;
	
	private String description;
	
	private Long[] categoryIds;
	
	private String logoUrl;
	
	private float rate;
	
	private long year;
	
	
	
	

}
