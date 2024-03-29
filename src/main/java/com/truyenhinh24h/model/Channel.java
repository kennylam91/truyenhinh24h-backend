package com.truyenhinh24h.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.truyenhinh24h.common.ImportSource;

import lombok.Data;

@Document(collection = "channels")
@Data
public class Channel {

	@Transient
	public static final String SEQUENCE_NAME = "channel_sequence";

	@Id
	private Long id;

	private String name;

	private String description;

	private String logo;

	private boolean vip;

	private String category;
	
	private Boolean hasAutoImport = false;
	
	private ImportSource importSource;
	
	private String sctvChannelCode;
	
	private Boolean isProgramAutoSearch = false;

}
