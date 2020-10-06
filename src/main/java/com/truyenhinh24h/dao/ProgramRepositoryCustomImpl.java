package com.truyenhinh24h.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.truyenhinh24h.controller.ProgramForm;
import com.truyenhinh24h.model.Program;
import com.truyenhinh24h.model.Schedule;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProgramRepositoryCustomImpl implements ProgramRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Page<Program> search(ProgramForm programForm, Pageable pageable) throws IllegalArgumentException {
		if (pageable == null) {
			throw new IllegalArgumentException("Pageable must not be null");
		}
		if ((programForm.getStartTime() != null && programForm.getEndTime() == null)
				|| programForm.getStartTime() == null && programForm.getEndTime() != null) {
			throw new IllegalArgumentException("StartTime and EndTime must be together");
		}
		final Query query = new Query();
		if (programForm.getSearchName() != null) {
			Criteria nameCriteria = where("name").regex(programForm.getSearchName());
			Criteria enNameCriterial = where("enName").regex(programForm.getSearchName());
			query.addCriteria(new Criteria().orOperator(nameCriteria, enNameCriterial));
		}
		if (programForm.getCategoryCodes() != null) {
			Criteria categoryCriteria = where("categoryCodes")
					.elemMatch(new Criteria().in(Arrays.asList(programForm.getCategoryCodes())));
			query.addCriteria(categoryCriteria);
		}
		if(programForm.getRank() != null) {
			query.addCriteria(where("rank").is(programForm.getRank()));
		}
		if (programForm.getStartTime() != null && programForm.getEndTime() != null) {
			Query scheduleQuery = new Query();
			scheduleQuery.addCriteria(where("startTime").gte(programForm.getStartTime()))
					.addCriteria(where("endTime").lte(programForm.getEndTime()));
			List<Schedule> schedules = mongoTemplate.find(scheduleQuery, Schedule.class);
			Set<Long> scheduleIds = schedules.stream().map(Schedule::getProgramId).collect(Collectors.toSet());
			query.addCriteria(where("_id").in(scheduleIds));
		}
		query.with(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
		List<Program> programs = mongoTemplate.find(query, Program.class);
		long total = mongoTemplate.count(query, Program.class);
		return new PageImpl<>(programs, pageable, total);
	}

}
