package com.truyenhinh24h.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truyenhinh24h.model.Schedule;
import com.truyenhinh24h.model.ScheduleDto;
import com.truyenhinh24h.service.ScheduleService;

@RestController
@RequestMapping(path = "/rest/v1/schedules")
public class ScheduleController {

	private static final Logger logger = LogManager.getLogger(ScheduleController.class);

	@Autowired
	private ScheduleService scheduleService;

	@PostMapping
	public ResponseEntity<ScheduleDto> createOrUpdate(@Valid @RequestBody ScheduleForm scheduleForm) {
		logger.info("Create Schedule");
		Schedule schedule = mapper(scheduleForm);
		try {
			ScheduleDto result = scheduleService.createOrUpdate(schedule);
			logger.info("Schedule created: {}", result);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			logger.error(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/import")
	public ResponseEntity<Void> importMulti(@Valid @RequestBody List<ScheduleForm> forms) {
		try {
			List<Schedule> schedules = forms.stream().map(this::mapper).collect(Collectors.toList());
			scheduleService.importMulti(schedules);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping(path = "/search")
	public ResponseEntity<Page<ScheduleDto>> search(@RequestBody ScheduleForm scheduleForm,
			HttpServletRequest request) {
		try {
			Page<ScheduleDto> scheduleDtoPage = scheduleService.search(scheduleForm);
			return ResponseEntity.ok(scheduleDtoPage);
		} catch (Exception e) {
			logger.error(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping(path="/delete-multi")
	public ResponseEntity<Void> deleteMulti(@RequestBody ScheduleForm scheduleForm) {
		if(scheduleForm.getScheduleIds() == null || scheduleForm.getScheduleIds().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		try {
			scheduleService.deleteMulti(scheduleForm.getScheduleIds());
			logger.info("Deleted Program : {}", scheduleForm.getScheduleIds());
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private Schedule mapper(ScheduleForm data) {
		if (data == null) {
			return null;
		}
		Schedule schedule = new Schedule();
		schedule.setChannelId(data.getChannelId());
		schedule.setChannelName(data.getChannelName());
		schedule.setEndTime(data.getEndTime());
		schedule.setProgramId(data.getProgramId());
		schedule.setProgramName(data.getProgramName());
		schedule.setId(data.getId());
		schedule.setStartTime(data.getStartTime());
		return schedule;

	}

	
}
