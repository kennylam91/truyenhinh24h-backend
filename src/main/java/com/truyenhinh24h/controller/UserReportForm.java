package com.truyenhinh24h.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserReportForm {

	private Long id;

	@NotNull
	private Long channelId;

	private String content;

	private Date time = new Date();
	
	public String getDateInGMTPlus7(){
		LocalDate localDate = time.toInstant()
				.atZone(ZoneId.of("+7"))
				.toLocalDate();
		return localDate.toString();
	}
}
