package com.truyenhinh24h.controller;

import java.util.ArrayList;
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
import com.truyenhinh24h.dao.StatsData;
import com.truyenhinh24h.model.Schedule;
import com.truyenhinh24h.model.ScheduleDto;
import com.truyenhinh24h.service.ChannelService;
import com.truyenhinh24h.service.CommonService;
import com.truyenhinh24h.service.ScheduleService;

@RestController
@RequestMapping(path = "/rest/v1/schedules")
public class ScheduleController {

	public static final String UTC_PLUS_7 = "UTC+07:00";

	private static final Logger logger = LogManager.getLogger(ScheduleController.class);

	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private ChannelService channelService;

	@PostMapping
	public ResponseEntity<ScheduleDto> createOrUpdate(@Valid @RequestBody ScheduleForm scheduleForm) {
		logger.info("Create Schedule");
		Schedule schedule = mapper(scheduleForm);
		ScheduleDto result = scheduleService.createOrUpdate(schedule);
		logger.info("Schedule created: {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/import")
	public ResponseEntity<Void> importMulti(@Valid @RequestBody List<ScheduleForm> forms) {
		List<Schedule> schedules = forms.stream().map(this::mapper).collect(Collectors.toList());
		scheduleService.importMulti(schedules);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(path ="/auto-update")
	public ResponseEntity<Void> autoUpdateSchedules(@RequestBody ScheduleForm form) throws Exception {
		List<Schedule> scheduleList = new ArrayList<>();
		if (form.getChannelName().contains("THVL")) {
			scheduleList = commonService.getScheduleListFromTHVL(form);
		} else if (form.getApiSource().contentEquals("SCTV")) {
			scheduleList = commonService.getScheduleListFromSCTV(form);
		} else if(form.getApiSource().contentEquals("VTV")) {
			scheduleList = commonService.getScheduleListFromVTV(form);
		}

		if (!scheduleList.isEmpty()) {
			scheduleService.importMulti(scheduleList);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@PostMapping(path = "/search")
	public ResponseEntity<Page<ScheduleDto>> search(@RequestBody ScheduleForm scheduleForm,
			HttpServletRequest request) {
		Page<ScheduleDto> scheduleDtoPage = scheduleService.search(scheduleForm);
		return ResponseEntity.ok(scheduleDtoPage);
	}

	@PostMapping(path = "/delete-multi")
	public ResponseEntity<Void> deleteMulti(@RequestBody ScheduleForm scheduleForm) {
		if (scheduleForm.getScheduleIds() == null || scheduleForm.getScheduleIds().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		scheduleService.deleteMulti(scheduleForm.getScheduleIds());
		logger.info("Deleted Program : {}", scheduleForm.getScheduleIds());
		return ResponseEntity.ok().build();
	}

	@PostMapping(path = "/stats")
	public ResponseEntity<List<StatsData>> getScheduleStats(@RequestBody ScheduleForm form) {
		return ResponseEntity.ok(scheduleService.getScheduleStats(form));
	}
	
	@PostMapping(path = "/clear-cache")
	public ResponseEntity<Void> clearScheduleCache(){
		scheduleService.clearCache();
		return ResponseEntity.ok().build();
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
