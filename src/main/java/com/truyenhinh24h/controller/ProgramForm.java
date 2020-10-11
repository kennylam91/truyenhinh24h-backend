package com.truyenhinh24h.controller;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.truyenhinh24h.common.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramForm extends BaseForm {

	private Long id;

	@NotEmpty(message = "Name must not be empty")
	private String name;

	private String enName;

	private String description;

	// su dung khi import du lieu tu firestore vao
//	private Long[] categories;
	
	private Long rank = 1L;

	private List<Long> ranks;

	private long year;

	private Long[] programIds;

	private String searchName;

	private Date startTime;

	private Date endTime;

	private String trailer;

	private String logo;

	private Long[] categoryCodes;

	private Date startTimeFrom;

	private Date startTimeTo;

	// Dung de search cac program dang chieu
	private Boolean isBroadCasting;

	public boolean isStartTimeFilterValid() {
		return ((startTimeFrom != null) && (startTimeTo != null)) || 
				((startTimeFrom == null) && (startTimeTo == null));
	}
	public String getOnlyTextName() {
		return name.replaceAll(Utils.SYMBOL_REGEX, "") + " " + 
				enName.replaceAll(Utils.SYMBOL_REGEX, "");
	}
}
