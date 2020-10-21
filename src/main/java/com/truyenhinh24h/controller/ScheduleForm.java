package com.truyenhinh24h.controller;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ScheduleForm extends BaseForm {

	private Long id;

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

	private List<Long> scheduleIds;

	private Date startTimeFrom;

	private Date startTimeTo;

	private String updateDate;
	
	
	
	
	
}
