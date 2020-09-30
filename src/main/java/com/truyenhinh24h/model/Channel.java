package com.truyenhinh24h.model;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "channels")
@Data
public class Channel {

	@Transient
	public static final String SEQUENCE_NAME = "channel_sequence";

	@Id
	private Long channelId;

	private String name;

	private String description;

	Set<String> categories;

	private String logoUrl;

	private boolean vip;

	private Long networkId;

}
