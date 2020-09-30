package com.truyenhinh24h.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.truyenhinh24h.dao.ScheduleRepository;
import com.truyenhinh24h.model.Schedule;
import com.truyenhinh24h.model.ScheduleDto;

@Service
public class ScheduleService {
	
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;
	
	public ScheduleDto createOrUpdate(ScheduleDto scheduleDto) {
		Schedule schedule = mapper(scheduleDto);
		Schedule result = null;
		if(schedule.getScheduleId() == null) {
			schedule.setScheduleId(sequenceGeneratorService.generateSequence(Schedule.SEQUENCE_NAME));
			result = scheduleRepository.insert(schedule);
		} else {
			result = scheduleRepository.save(schedule);
		}
		return mapper(result);
	}
	
	public Page<ScheduleDto> search(Pageable pageable, ScheduleDto scheduleDto) {
		Page<Schedule> schedulePage = scheduleRepository.search(scheduleDto, pageable);
		if (!schedulePage.hasContent()) {
			return new PageImpl<>(Collections.emptyList(), pageable, 0);
		} else {
			List<Schedule> scheduleList = schedulePage.getContent();
			List<ScheduleDto> scheduleDtoList = scheduleList.stream().map(this::mapper).collect(Collectors.toList());
			return new PageImpl<>(scheduleDtoList, pageable, schedulePage.getTotalElements());
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
		schedule.setScheduleId(data.getScheduleId());
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
		schedule.setScheduleId(data.getScheduleId());
		schedule.setStartTime(data.getStartTime());
		return schedule;
	}

}
