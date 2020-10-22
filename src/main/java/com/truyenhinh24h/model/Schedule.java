package com.truyenhinh24h.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "schedules")
public class Schedule {

	@Transient
	public static final String SEQUENCE_NAME = "schedule_sequence";

	@Id
	private Long id;

	@Indexed(name = "schedule_channelId_index")
	private Long channelId;

	@Indexed(name = "schedule_programId_index")
	private Long programId;

	@Indexed(name = "schedule_startTime_index", expireAfterSeconds = 604800)
	private Date startTime;

	@Indexed(name = "schedule_endTime_index", expireAfterSeconds = 604800)
	private Date endTime;

	private String programName;

	private String channelName;

}
