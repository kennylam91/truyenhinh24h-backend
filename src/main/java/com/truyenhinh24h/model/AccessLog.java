package com.truyenhinh24h.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpMethod;

import lombok.Data;

@Document(collection = "access_logs")
@Data
public class AccessLog {

	@Transient
	public static final String SEQUENCE_NAME = "access_log_sequence";
	
	@Id
	private Long id;
	
	@Indexed(name = "log_created_at_index", direction = IndexDirection.ASCENDING)
	private Date createdAt;
	
	@Indexed(name = "log_endPoint_index")
	private String endPoint;
	
	private String ip;
	
	private HttpMethod method;
}
