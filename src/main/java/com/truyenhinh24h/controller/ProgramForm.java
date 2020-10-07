package com.truyenhinh24h.controller;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

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
	
	@NotEmpty(message = "Name must not be empty")
	private String name;
	
	private String enName;
	
	private String description;
	
	// su dung khi import du lieu tu firestore vao
//	private Long[] categories;
	
	private Long rank;
	
	private long year;
	
	private Long[] programIds;
	
	private String searchName;
	
	private Date startTime;
	
	private Date endTime;
	
	private String trailer;
	
	private String logo;
	
	private Long[] categoryCodes;
}
