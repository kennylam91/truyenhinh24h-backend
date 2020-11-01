package com.truyenhinh24h.model.sctv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SctvResponseBody {

	@JsonProperty("LichPhatSong")
	private SctvSchedule LichPhatSong;
	
	@JsonProperty("End")
	private Boolean End;
	
	@JsonProperty("Ext")
	private String Ext;
}
