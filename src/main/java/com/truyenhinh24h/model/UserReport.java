package com.truyenhinh24h.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "user_reports")
public class UserReport {

	@Transient
	public static final String SEQUENCE_NAME = "user_reports_sequence";
	
	@Id
	private Long id;
	
	@Indexed(name = "user_reports_channel_id", expireAfterSeconds = 604800)
	private Long channelId;
	
	private String content;
	
	private Date time;
}
