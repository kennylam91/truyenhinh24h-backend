package com.truyenhinh24h.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truyenhinh24h.model.UserReportDto;
import com.truyenhinh24h.service.UserReportService;
import com.truyenhinh24h.utils.Mapper;

@RestController
@RequestMapping(path = "/rest/v1/user-reports")
public class UserReportController {

	@Autowired
	private UserReportService userReportService;
	@Autowired
	private Mapper mapper;

	private static final Logger logger = LoggerFactory.getLogger(UserReportController.class);

	@PostMapping
	public ResponseEntity<UserReportDto> create(@Valid @RequestBody UserReportForm userReportForm) {
		logger.info("Create user report form");
		return ResponseEntity.ok(userReportService.create(userReportForm));
	}

	@PostMapping("/get-all")
	public ResponseEntity<Page<UserReportDto>> findAll(@RequestBody BaseForm request) {
		return ResponseEntity.ok(userReportService.findAll(request));
	}
}
