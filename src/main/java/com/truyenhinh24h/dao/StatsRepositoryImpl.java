package com.truyenhinh24h.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Repository;

import com.truyenhinh24h.controller.StatsForm;
import com.truyenhinh24h.exception.InvalidInputException;

@Repository
public class StatsRepositoryImpl implements StatsRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	private final Logger logger = LoggerFactory.getLogger(StatsRepository.class);

	@Override
	public List<StatsData> getScheduleChannelStats(StatsForm form) {
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(where("startTime").gte(form.getStartTimeFrom())
						.andOperator(where("startTime").lte(form.getStartTimeTo()))),
				Aggregation.project("channelId", "startTime")
						.andExpression(
								"{$dateToString: " + "{date: \"$startTime\", format: \"%Y-%m-%d\", timezone: \"+07\"}}")
						.as("date"),
				Aggregation.group("channelId", "date").count().as("total"),
				Aggregation.project("total").and("_id.channelId").as("channelId").and("_id.date").as("date")

		);

		List<StatsData> list = mongoTemplate.aggregate(aggregation, "schedules", StatsData.class).getMappedResults();
		return list;
	}

}
