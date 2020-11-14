package com.truyenhinh24h.controller;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsForm {

	private Date startTimeFrom;

	private Date startTimeTo;
}
