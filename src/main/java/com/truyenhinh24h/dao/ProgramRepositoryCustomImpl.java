package com.truyenhinh24h.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.truyenhinh24h.common.Utils;
import com.truyenhinh24h.controller.ProgramForm;
import com.truyenhinh24h.model.Program;
import com.truyenhinh24h.model.ProgramDto;
import com.truyenhinh24h.model.Schedule;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ProgramRepositoryCustomImpl implements ProgramRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Page<ProgramDto> search(ProgramForm programForm, Pageable pageable) {
		final Query query = new Query();
		if (programForm.getSearchName() != null && programForm.getSearchName().length() >=3) {
			query.addCriteria(where("onlyTextName")
					.regex(programForm.getSearchName().replaceAll(Utils.SYMBOL_REGEX, "")));
		}
		if (programForm.getCategoryCodes() != null && programForm.getCategoryCodes().length > 0) {
			Criteria categoryCriteria = where("categoryCodes")
					.elemMatch(new Criteria().in(Arrays.asList(programForm.getCategoryCodes())));
			query.addCriteria(categoryCriteria);
		}
		if (programForm.getRanks() != null && !programForm.getRanks().isEmpty()) {
			query.addCriteria(where("rank").in(programForm.getRanks()));
		}
		Query scheduleQuery = new Query();
		List<Schedule> schedules = null;
		if (programForm.getIsBroadCasting() != null && programForm.getIsBroadCasting().booleanValue()) {
			scheduleQuery.addCriteria(where("startTime").lte(new Date()))
				.addCriteria(where("endTime").gte(new Date()));
			schedules = mongoTemplate.find(scheduleQuery, Schedule.class);
		} else if (programForm.getStartTimeFrom() != null && programForm.getStartTimeTo() != null) {
			scheduleQuery.addCriteria(where("startTime").gte(programForm.getStartTimeFrom())
					.andOperator(where("startTime").lte(programForm.getStartTimeTo())));
			schedules = mongoTemplate.find(scheduleQuery, Schedule.class);
		}
		if(schedules != null) {
			Set<Long> scheduleIds = schedules.stream().map(Schedule::getProgramId).collect(Collectors.toSet());
			query.addCriteria(where("_id").in(scheduleIds));
		}
		
		long total = mongoTemplate.count(query, Program.class);
		query.with(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
		List<Program> programs = mongoTemplate.find(query, Program.class);
		List<ProgramDto> programDtoList = programs.stream()
				.map(Program::getDto).collect(Collectors.toList());
		for (ProgramDto programDto : programDtoList) {
			if (schedules != null && !schedules.isEmpty()) {
				List<Schedule> foundSchedules = schedules.stream()
						.filter(s -> Objects.equals(s.getProgramId(), programDto.getId()))
						.collect(Collectors.toList());
				programDto.setSchedules(foundSchedules);
			}
		}

		return new PageImpl<>(programDtoList, pageable, total);
	}

}
