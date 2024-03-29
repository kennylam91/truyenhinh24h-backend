package com.truyenhinh24h.model.sctv;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.truyenhinh24h.controller.ScheduleForm;
import com.truyenhinh24h.model.Schedule;

import lombok.Data;

@Data
public class SctvEvent {
	
	Logger logger = LoggerFactory.getLogger(SctvEvent.class);
	
	@JsonProperty("StartTime")
	private String StartTime;
	
	@JsonProperty("EndTime")
	private String EndTime;
	
	@JsonProperty("Name")
	private String Name;
	
	@JsonProperty("ShortDescriptor")
	private String ShortDescriptor;
	
	@JsonProperty("ExtendedDescriptor")
	private String ExtendedDescriptor;
	
	@JsonProperty("Hot")
	private Boolean Hot;
	
	private String[] getStartTimeArr() {
		return StartTime.split(":");
	}

	private int getStartTimeHour() {
		return Integer.parseInt(getStartTimeArr()[0]);
	}

	public Schedule mapper(ScheduleForm form) {
		try {
			Schedule schedule = new Schedule();
			schedule.setProgramName(Name);
			LocalDateTime startTime = form.getImportDate().atTime(LocalTime.parse(StartTime));
			LocalDateTime endTime = form.getImportDate().atTime(LocalTime.parse(EndTime));
			schedule.setStartTime(Date.from(startTime.atZone(ZoneId.of("UTC+07:00")).toInstant()));
			schedule.setEndTime(Date.from(endTime.atZone(ZoneId.of("UTC+07:00")).toInstant()));
			schedule.setChannelId(form.getChannelId());
			schedule.setChannelName(form.getChannelName());
			return schedule;
		} catch (DateTimeParseException e) {
			logger.error(e.getMessage() + " " + form.getChannelName() + " " + form.getProgramName());
			return null;
		}
	}
}
