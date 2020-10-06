package com.truyenhinh24h.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "programs")
@Data
public class Program {
	
	@Transient
	public static final String SEQUENCE_NAME = "program_sequence";
	
	@Id
	private Long id;
	
	@Indexed(name = "program_name_index", background = true, direction = IndexDirection.ASCENDING)
	private String name;
	
	@Indexed(name="program_enName_index")
	private String enName;
	
	private String description;
	
	// category codes
	private Long[] categoryCodes;
	
	private String logo;
	
	private Long rank;
	
	private long year;
	
	private String trailer;
	
	
	
	

}
