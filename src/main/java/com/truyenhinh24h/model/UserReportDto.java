package com.truyenhinh24h.model;

import java.util.Date;

import lombok.Data;

@Data
public class UserReportDto {

	private Long id;

	private Long channelId;

	private String content;

	private Date time;
}
