package com.truyenhinh24h.model;

import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class ProgramDto {

	private Long id;
	
	private String name;
	
	private String enName;
	
	private String description;
	
	// category codes
	private Long[] categoryCodes;
	
	private String logo;
	
	private Long rank;
	
	private long year;
	
	private Set<Category> categories;
	
	private String trailer;
	
	// dung cho chuong trinh dang phat
	private List<Schedule> schedules;
	
}
