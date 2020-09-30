package com.truyenhinh24h.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.truyenhinh24h.model.Schedule;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, Long>{

}
