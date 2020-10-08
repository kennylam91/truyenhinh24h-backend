package com.truyenhinh24h.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truyenhinh24h.dao.AccessLogRepository;
import com.truyenhinh24h.model.AccessLog;

@Service
public class AccessLogService {
	
	@Autowired
	private AccessLogRepository accessLogRepository;
	
	@Autowired
	private SequenceGeneratorService sequenceGenService;
	
	public void createOrUpdate(AccessLog log) {
		log.setId(sequenceGenService.generateSequence(AccessLog.SEQUENCE_NAME));
		accessLogRepository.save(log);
	}

}
