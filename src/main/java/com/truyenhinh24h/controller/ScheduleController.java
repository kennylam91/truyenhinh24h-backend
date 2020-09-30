package com.truyenhinh24h.controller;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truyenhinh24h.model.CategoryDto;
import com.truyenhinh24h.model.ScheduleDto;
import com.truyenhinh24h.service.ScheduleService;

@RestController
@RequestMapping(path = "/rest/v1/schedules")
public class ScheduleController {

	private static final Logger logger = LogManager.getLogger(ScheduleController.class);
	
	@Autowired
	private ScheduleService scheduleService;
	
	@PostMapping
	public ResponseEntity<ScheduleDto> createOrUpdate(@Valid @RequestBody ScheduleForm scheduleForm){
		logger.info("Create Schedule");
		ScheduleDto scheduleDto = mapper(scheduleForm);
		try {
			ScheduleDto result = scheduleService.createOrUpdate(scheduleDto);
			logger.info("Schedule created: {}", result);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			logger.error(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
//	@PostMapping
//	public ResponseEntity<Page<ScheduleDto>> getAll(@RequestBody ScheduleForm scheduleForm){
//		try {
//			Sort sort = Sort.by(Sort.Direction.ASC, "startTime");
//			Pageable pageable = PageRequest.of(scheduleForm.getPage() -1 , scheduleForm.getLimit(), sort);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
	
	private ScheduleDto mapper(ScheduleForm data) {
		if(data == null) {
			return null;
		}
		ScheduleDto schedule = new ScheduleDto();
		schedule.setChannelId(data.getChannelId());
		schedule.setChannelName(data.getChannelName());
		schedule.setEndTime(data.getEndTime());
		schedule.setProgramId(data.getProgramId());
		schedule.setProgramName(data.getProgramName());
		schedule.setScheduleId(data.getScheduleId());
		schedule.setStartTime(data.getStartTime());
		return schedule;
		
	}
}