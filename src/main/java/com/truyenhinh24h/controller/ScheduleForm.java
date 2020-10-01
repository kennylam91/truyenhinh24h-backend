package com.truyenhinh24h.controller;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Setter
@NoArgsConstructor
public class ScheduleForm extends BaseForm {
	
	private Long scheduleId;

	@NotNull(message = "ChannelId must not be null")
	private Long channelId;

	private Long programId;

	@NotNull(message = "StartTime must not be null")
	private Date startTime;

	@NotNull(message = "EndTime must not be null")
	private Date endTime;

	@NotEmpty(message = "ProgramName must not be null")
	private String programName;

	@NotEmpty(message = "ChannelName must not be null")
	private String channelName;

}
