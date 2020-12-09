package com.truyenhinh24h.controller;

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
}
