package com.truyenhinh24h.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.truyenhinh24h.model.Schedule;
import com.truyenhinh24h.model.ScheduleDto;

public interface ScheduleRepositoryCustom {

	Page<Schedule> search(ScheduleDto scheduleDto, Pageable pageable);
}
