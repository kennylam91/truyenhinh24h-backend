package com.truyenhinh24h.model;

import java.util.Set;

import lombok.Data;

@Data
public class ProgramDto {

	private Long id;
	
	private String name;
	
	private String enName;
	
	private String description;
	
	private Long[] categoryIds;
	
	private String logoUrl;
	
	private Long rank;
	
	private long year;
	
	private Set<Category> categories;
	
	private String trailerUrl;
}
