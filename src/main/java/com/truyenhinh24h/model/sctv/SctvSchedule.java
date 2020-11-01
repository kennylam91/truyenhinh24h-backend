package com.truyenhinh24h.model.sctv;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SctvSchedule {
	
	@JsonProperty("ChannelId")
	private Long ChannelId;
	
	@JsonProperty("Date")
	private String Date;
	
	@JsonProperty("EventList")
	private List<SctvEvent> EventList;

}
