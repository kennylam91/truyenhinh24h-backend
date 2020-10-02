package com.truyenhinh24h.controller;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramForm extends BaseForm {

	private Long id;
	
	private String name;
	
	private String enName;
	
	private String description;
	
	private Long[] categoryIds;
	
	private String logoUrl;
	
	private float rate;
	
	private long year;
	
	private Long[] programIds;
	
	private String searchName;
	
	private Date startTime;
	
	private Date endTime;
}
