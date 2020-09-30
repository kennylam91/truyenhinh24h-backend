package com.truyenhinh24h.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.truyenhinh24h.model.Schedule;
import com.truyenhinh24h.model.ScheduleDto;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

public class ScheduleRepositoryCustomImpl implements ScheduleRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Page<Schedule> search(ScheduleDto scheduleDto, Pageable pageable) {
		final Query query = new Query();
		if (scheduleDto != null) {
			if (scheduleDto.getChannelId() != null) {
				query.addCriteria(where("channelId").is(scheduleDto.getChannelId()));
			}
		}
		query.with(Sort.by(Sort.Direction.DESC, "startTime"));
		if (pageable != null) {
			query.with(PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize()));
		}
		List<Schedule> schedules = mongoTemplate.find(query, Schedule.class);
		long total = mongoTemplate.count(query, Schedule.class);
		return new PageImpl<>(schedules, pageable, total);
	}

}
