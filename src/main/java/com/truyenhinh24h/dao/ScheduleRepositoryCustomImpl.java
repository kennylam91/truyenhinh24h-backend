package com.truyenhinh24h.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.truyenhinh24h.controller.ScheduleForm;
import com.truyenhinh24h.model.Schedule;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

public class ScheduleRepositoryCustomImpl implements ScheduleRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Page<Schedule> search(ScheduleForm scheduleForm, Pageable pageable) {
		final Query query = new Query();
		
		Criteria timeCriteria = where("startTime").gte(scheduleForm.getStartTime()).andOperator(
				where("startTime").lte(scheduleForm.getEndTime()));
		if(scheduleForm.getStartTime() != null && scheduleForm.getEndTime() == null) {
			timeCriteria = where("startTime").gte(scheduleForm.getStartTime());
		}
		if (scheduleForm != null) {
			if (scheduleForm.getChannelId() != null) {
				query.addCriteria(where("channelId").is(scheduleForm.getChannelId()));
			}
			if (scheduleForm.getProgramId() != null) {
				query.addCriteria(where("programId").is(scheduleForm.getProgramId()));
			}
			if (scheduleForm.getStartTime() != null) {
				query.addCriteria(timeCriteria);
			}
		}
		query.with(pageable);
		List<Schedule> schedules = mongoTemplate.find(query, Schedule.class);
		long total = mongoTemplate.count(query, Schedule.class);
		return new PageImpl<>(schedules, pageable, total);
	}

	@Override
	public List<StatsData> getStats(ScheduleForm form) {
		if (form.getStartTimeFrom() == null || form.getStartTimeTo() == null) {
			throw new IllegalArgumentException("startTimeFrom and startTimeTo must be not null");
		} else {
			try {
				Aggregation aggregation = Aggregation.newAggregation(
						Aggregation.match(where("startTime").gte(form.getStartTimeFrom())
								.andOperator(where("startTime").lte(form.getStartTimeTo()))),
						Aggregation.project("channelId", "startTime")
						.andExpression("{$dateToString: "
								+ "{date: \"$startTime\", format: \"%Y-%m-%d\", timezone: \"+07\"}}")
								.as("date"),
						Aggregation.group("channelId", "date").count().as("total"),
						Aggregation.project("total").and("_id.channelId").as("channelId")
								.and("_id.date").as("date")

				);

				List<StatsData> list = mongoTemplate.aggregate(aggregation, "schedules", StatsData.class)
						.getMappedResults();
				return list;
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}

		}
	}

	@Override
	public void insertAll(List<Schedule> scheduleList) {
		mongoTemplate.insertAll(scheduleList);
		
	}
	
	
	

}
