package com.truyenhinh24h.controller;

import java.util.Set;

import lombok.Value;

@Value
public class ProgramForm {

	private Long programId;
	
	private String name;
	
	private String description;
	
	private Set<Long> categoryIds;
	
	private String logoUrl;
	
	private float rate;
	
	private long year;
}
