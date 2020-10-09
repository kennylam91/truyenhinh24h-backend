package com.truyenhinh24h.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.truyenhinh24h.controller.ScheduleForm;
import com.truyenhinh24h.model.Schedule;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

public class ScheduleRepositoryCustomImpl implements ScheduleRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Page<Schedule> search(ScheduleForm scheduleForm, Pageable pageable) throws IllegalArgumentException {
		if (pageable == null) {
			throw new IllegalArgumentException("Pageable must not be null");
		}
		final Query query = new Query();
		if (scheduleForm != null) {
			if (scheduleForm.getChannelId() != null) {
				query.addCriteria(where("channelId").is(scheduleForm.getChannelId()));
			}
			if (scheduleForm.getProgramId() != null) {
				query.addCriteria(where("programId").is(scheduleForm.getProgramId()));
			}
			if (scheduleForm.getStartTime() != null && scheduleForm.getEndTime() != null) {
				query.addCriteria(where("startTime").gte(scheduleForm.getStartTime())
					 .andOperator(where("startTime").lte(scheduleForm.getEndTime())));
			}
		}
		query.with(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
		List<Schedule> schedules = mongoTemplate.find(query, Schedule.class);
		long total = mongoTemplate.count(query, Schedule.class);
		return new PageImpl<>(schedules, pageable, total);
	}

}
