package com.truyenhinh24h.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.truyenhinh24h.controller.ChannelForm;
import com.truyenhinh24h.controller.ScheduleForm;
import com.truyenhinh24h.dao.ScheduleRepository;
import com.truyenhinh24h.dao.StatsData;
import com.truyenhinh24h.model.Schedule;
import com.truyenhinh24h.model.ScheduleDto;
import com.truyenhinh24h.utils.Mapper;

@Service
public class ScheduleService {

	@Autowired
	private ScheduleRepository scheduleRepository;
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;
	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private Mapper mapper;

	@Caching(evict = { @CacheEvict(cacheNames = { "all-schedules" }, allEntries = true),
			@CacheEvict(cacheNames = { "programs-by-time" }, allEntries = true) })
	public ScheduleDto createOrUpdate(Schedule schedule) {
		Schedule result = null;
		if (schedule.getId() == null) {
			schedule.setId(sequenceGeneratorService.generateSequence(Schedule.SEQUENCE_NAME));
			result = scheduleRepository.insert(schedule);
		} else {
			result = scheduleRepository.save(schedule);
		}
		return mapper.fromTo(result, ScheduleDto.class);
	}

	@Cacheable(cacheNames = { "all-schedules" })
	public Page<ScheduleDto> search(ScheduleForm scheduleForm) {
		Pageable pageable = PageRequest.of(scheduleForm.getPage() - 1, scheduleForm.getLimit(),
				Sort.by(Sort.Direction.ASC, "startTime"));
		Page<Schedule> schedulePage = null;
		schedulePage = scheduleRepository.search(scheduleForm, pageable);
		if (schedulePage == null || !schedulePage.hasContent()) {
			return new PageImpl<>(Collections.emptyList(), pageable, 0);
		} else {
			List<Schedule> scheduleList = schedulePage.getContent();
			List<ScheduleDto> scheduleDtoList = mapper.fromToList(scheduleList, ScheduleDto.class);
			return new PageImpl<>(scheduleDtoList, pageable, schedulePage.getTotalElements());
		}
	}

	@Transactional
	@Caching(evict = { @CacheEvict(cacheNames = { "all-schedules" }, allEntries = true),
			@CacheEvict(cacheNames = { "programs-by-time" }, allEntries = true) })
	public void importMulti(List<Schedule> schedules) {
		for (Schedule schedule : schedules) {
			if (schedule.getId() == null) {
				schedule.setId(sequenceGeneratorService.generateSequence(Schedule.SEQUENCE_NAME));
			}
		}
		scheduleRepository.insertAll(schedules);
	}

	@Caching(evict = { @CacheEvict(cacheNames = { "all-schedules" }, allEntries = true),
			@CacheEvict(cacheNames = { "programs-by-time" }, allEntries = true) })
	public void deleteMulti(List<Long> scheduleIds) {
		scheduleRepository.deleteByIdIn(scheduleIds);

	}

	public void clearCache() {
		cacheManager.getCache("all-schedules").clear();
		cacheManager.getCache("programs-by-time").clear();
	}

	public List<StatsData> getScheduleStats(ScheduleForm form) {
		return scheduleRepository.getStats(form);
	}

}
