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

import java.util.List;


public class ProgramRepositoryCustomImpl implements ProgramRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public Page<Program> search(ProgramForm programForm, Pageable pageable) throws IllegalArgumentException {
		if (pageable == null) {
			throw new IllegalArgumentException("Pageable must not be null");
		}
		final Query query = new Query();
		if(programForm != null) {
			if(programForm.getName() != null) {
				Criteria nameCriteria = where("name").regex(programForm.getSearchName());
				Criteria enNameCriterial = where("enName").regex(programForm.getSearchName());
				query.addCriteria(new Criteria().orOperator(nameCriteria, enNameCriterial));
			}
		}
		query.with(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
		List<Program> programs = mongoTemplate.find(query, Program.class);
		long total = mongoTemplate.count(query, Program.class);
		return new PageImpl<>(programs, pageable, total);
	}

}
