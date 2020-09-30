package com.truyenhinh24h.model;

import java.util.Date;

import lombok.Data;

@Data
public class ScheduleDto {

private Long scheduleId;
	
	private Long channelId;
	
	private Long programId;
	
	private Date startTime;
	
	private Date endTime;
	
	private String programName;
	
	private String channelName; 

}
