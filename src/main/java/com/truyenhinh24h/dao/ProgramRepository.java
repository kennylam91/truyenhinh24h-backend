package com.truyenhinh24h.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.truyenhinh24h.model.Program;

@Repository
public interface ProgramRepository extends MongoRepository<Program, Long>, ProgramRepositoryCustom{

	void deleteByIdIn(Long[] ids);
}
