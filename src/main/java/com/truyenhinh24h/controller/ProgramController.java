package com.truyenhinh24h.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truyenhinh24h.model.ProgramDto;
import com.truyenhinh24h.service.ProgramService;

@RestController
@RequestMapping(path = "/rest/v1/programs")
public class ProgramController {
	
	private static final Logger logger = LogManager.getLogger(ProgramController.class);
	
	@Autowired
	private ProgramService programService;
	
	@PostMapping
	public ResponseEntity<ProgramDto> createOrUpdate(@RequestBody ProgramForm programForm){
		logger.info("Create program");
		ProgramDto programDto = mapper(programForm);
		try {
			ProgramDto result = programService.createOrUpdate(programDto);
			logger.info("Program created: {}", result);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			logger.error(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	private ProgramDto mapper(ProgramForm program) {
		if(program == null) {
			return null;
		}
		ProgramDto programDto = new ProgramDto();
		programDto.setCategoryIds(program.getCategoryIds());
		programDto.setDescription(program.getDescription());
		programDto.setLogoUrl(program.getLogoUrl());
		programDto.setName(program.getName());
		programDto.setProgramId(program.getProgramId());
		programDto.setRate(program.getRate());
		programDto.setYear(program.getYear());
		
		return programDto;
	}

}
