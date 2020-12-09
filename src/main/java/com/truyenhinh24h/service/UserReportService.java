package com.truyenhinh24h.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import com.truyenhinh24h.controller.BaseForm;
import com.truyenhinh24h.controller.UserReportForm;
import com.truyenhinh24h.dao.UserReportRepository;
import com.truyenhinh24h.model.UserReport;
import com.truyenhinh24h.model.UserReportDto;
import com.truyenhinh24h.utils.Mapper;

@Service
public class UserReportService {

	@Autowired
	private UserReportRepository userReportRepository;
	@Autowired
	private Mapper mapper;
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	public UserReportDto create(UserReportForm userReportForm) {
		userReportForm.setId(sequenceGeneratorService.generateSequence(UserReport.SEQUENCE_NAME));
		UserReport entity = mapper.fromTo(userReportForm, UserReport.class);
		return mapper.fromTo(userReportRepository.save(entity), UserReportDto.class);
	}

	public Page<UserReportDto> findAll(BaseForm requestForm) {
		Page<UserReport> page = userReportRepository.findAll(requestForm.getPageable());
		List<UserReportDto> dtoList = mapper.fromToList(page.getContent(), UserReportDto.class);
		return new PageImpl<>(dtoList, requestForm.getPageable(), page.getTotalElements());
	}
}
