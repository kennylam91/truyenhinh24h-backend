package com.truyenhinh24h.controller;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((channelId == null) ? 0 : channelId.hashCode());
		result = prime * result + ((channelName == null) ? 0 : channelName.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((programId == null) ? 0 : programId.hashCode());
		result = prime * result + ((programName == null) ? 0 : programName.hashCode());
		result = prime * result + ((scheduleIds == null) ? 0 : scheduleIds.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((startTimeFrom == null) ? 0 : startTimeFrom.hashCode());
		result = prime * result + ((startTimeTo == null) ? 0 : startTimeTo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScheduleForm other = (ScheduleForm) obj;
		if (channelId == null) {
			if (other.channelId != null)
				return false;
		} else if (!channelId.equals(other.channelId))
			return false;
		if (channelName == null) {
			if (other.channelName != null)
				return false;
		} else if (!channelName.equals(other.channelName))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (programId == null) {
			if (other.programId != null)
				return false;
		} else if (!programId.equals(other.programId))
			return false;
		if (programName == null) {
			if (other.programName != null)
				return false;
		} else if (!programName.equals(other.programName))
			return false;
		if (scheduleIds == null) {
			if (other.scheduleIds != null)
				return false;
		} else if (!scheduleIds.equals(other.scheduleIds))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (startTimeFrom == null) {
			if (other.startTimeFrom != null)
				return false;
		} else if (!startTimeFrom.equals(other.startTimeFrom))
			return false;
		if (startTimeTo == null) {
			if (other.startTimeTo != null)
				return false;
		} else if (!startTimeTo.equals(other.startTimeTo))
			return false;
		return true;
	}

	
}
