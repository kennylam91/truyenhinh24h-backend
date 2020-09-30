package com.truyenhinh24h.model;

import java.util.Set;

import lombok.Data;

@Data
public class ProgramDto {

	private Long programId;
	
	private String name;
	
	private String description;
	
	private Set<Long> categoryIds;
	
	private String logoUrl;
	
	private float rate;
	
	private long year;
}
