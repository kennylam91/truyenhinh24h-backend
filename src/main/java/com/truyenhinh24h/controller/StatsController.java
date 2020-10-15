package com.truyenhinh24h.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truyenhinh24h.common.Utils;
import com.truyenhinh24h.dao.StatsData;
import com.truyenhinh24h.exception.InvalidInputException;
import com.truyenhinh24h.model.AccessLog;
import com.truyenhinh24h.service.AccessLogService;
import com.truyenhinh24h.service.StatsService;

@RestController
@RequestMapping(path = "/rest/v1")
public class StatsController {

	@Autowired
	private StatsService statsService;
	
	@Autowired
	private AccessLogService logService;

	@PostMapping("/stats/channels/total-schedules-daily")
	public ResponseEntity<List<StatsData>> getScheduleChannelStats(@RequestBody StatsForm form)
			throws InvalidInputException {
		if (form.getStartTimeFrom() == null || form.getStartTimeTo() == null) {
			throw new InvalidInputException("startTimeFrom and startTimeTo must be not null");
		} else {
			return ResponseEntity.ok(statsService.getScheduleChannelStats(form));
		}
	}
	@GetMapping(path = "/homepage")
	public ResponseEntity<Void> logHomepage(HttpServletRequest request){
		AccessLog log = new AccessLog();
		log.setCreatedAt(new Date());
		log.setEndPoint("/channels/get-all");
		log.setIp(Utils.getClientIpAddress(request));
		log.setMethod(HttpMethod.POST);
		logService.createOrUpdate(log);

		return ResponseEntity.ok().build();
	}
}
