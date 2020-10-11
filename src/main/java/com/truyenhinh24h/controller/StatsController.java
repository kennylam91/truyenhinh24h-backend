package com.truyenhinh24h.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truyenhinh24h.dao.StatsData;
import com.truyenhinh24h.exception.InvalidInputException;
import com.truyenhinh24h.service.StatsService;

@RestController
@RequestMapping(path = "/rest/v1/stats")
public class StatsController {

	@Autowired
	private StatsService statsService;

	@PostMapping("/channels/total-schedules-daily")
	public ResponseEntity<List<StatsData>> getScheduleChannelStats(@RequestBody StatsForm form)
			throws InvalidInputException {
		if (form.getStartTimeFrom() == null || form.getStartTimeTo() == null) {
			throw new InvalidInputException("startTimeFrom and startTimeTo must be not null");
		} else {
			return ResponseEntity.ok(statsService.getScheduleChannelStats(form));
		}
	}
}
