package com.truyenhinh24h.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.truyenhinh24h.controller.ScheduleForm;
import com.truyenhinh24h.dao.ScheduleRepository;
import com.truyenhinh24h.model.Schedule;
import com.truyenhinh24h.model.ScheduleDto;

@Service
public class ScheduleService {
	
	private static final Logger logger = LogManager.getLogger(ScheduleService.class);

	
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;
	
	public ScheduleDto createOrUpdate(Schedule schedule) {
		Schedule result = null;
		if(schedule.getId() == null) {
			schedule.setId(sequenceGeneratorService.generateSequence(Schedule.SEQUENCE_NAME));
			result = scheduleRepository.insert(schedule);
		} else {
			result = scheduleRepository.save(schedule);
		}
		return mapper(result);
	}
	
	public Page<ScheduleDto> search(ScheduleForm scheduleForm) {
		Pageable pageable = PageRequest.of(scheduleForm.getPage() - 1, scheduleForm.getLimit(), 
				Sort.by(Sort.Direction.ASC, "startTime"));
		Page<Schedule> schedulePage = null;
		try {
			schedulePage = scheduleRepository.search(scheduleForm, pageable);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		if (schedulePage == null || !schedulePage.hasContent()) {
			return new PageImpl<>(Collections.emptyList(), pageable, 0);
		} else {
			List<Schedule> scheduleList = schedulePage.getContent();
			List<ScheduleDto> scheduleDtoList = scheduleList.stream().map(this::mapper).collect(Collectors.toList());
			return new PageImpl<>(scheduleDtoList, pageable, schedulePage.getTotalElements());
		}
	}
	
	@Transactional
	public void importMulti(List<Schedule> schedules) {
		for (Schedule schedule : schedules) {
			if(schedule.getId() == null) {
				schedule.setId(sequenceGeneratorService.generateSequence(Schedule.SEQUENCE_NAME));
			}
			scheduleRepository.save(schedule);
		}
	}
	
	Schedule mapper(ScheduleDto data) {
		if(data == null) {
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
	
	ScheduleDto mapper(Schedule data) {
		if(data == null) {
			return null;
		}
		ScheduleDto schedule = new ScheduleDto();
		schedule.setChannelId(data.getChannelId());
		schedule.setChannelName(data.getChannelName());
		schedule.setEndTime(data.getEndTime());
		schedule.setProgramId(data.getProgramId());
		schedule.setProgramName(data.getProgramName());
		schedule.setId(data.getId());
		schedule.setStartTime(data.getStartTime());
		return schedule;
	}

	public void deleteMulti(List<Long> scheduleIds) {
		scheduleRepository.deleteByIdIn(scheduleIds);
		
	}

}
