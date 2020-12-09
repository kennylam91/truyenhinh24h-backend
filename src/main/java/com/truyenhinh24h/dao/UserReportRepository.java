package com.truyenhinh24h.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.truyenhinh24h.model.UserReport;

@Repository
public interface UserReportRepository extends MongoRepository<UserReport, Long> {

}
