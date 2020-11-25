package com.truyenhinh24h.model.htv;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class HtvResponseBody {

	@JsonProperty("success")
	private Boolean success;

	@JsonProperty("chanelUrl")
	private String chanelUrl;

	@JsonProperty("data")
	private String data;
}
