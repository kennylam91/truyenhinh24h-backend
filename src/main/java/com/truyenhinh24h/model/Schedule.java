package com.truyenhinh24h.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "schedules")
public class Schedule {
	
	@Transient
	public static final String SEQUENCE_NAME = "schedule_sequence";
	
	@Id
	private Long id;
	
	private Long channelId;
	
	private Long programId;
	
	private Date startTime;
	
	private Date endTime;
	
	private String programName;
	
	private String channelName; 

}
