package com.truyenhinh24h.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.truyenhinh24h.controller.ScheduleForm;
import com.truyenhinh24h.model.Schedule;

public interface ScheduleRepositoryCustom {

	Page<Schedule> search(ScheduleForm scheduleForm, Pageable pageable) throws Exception;
}
