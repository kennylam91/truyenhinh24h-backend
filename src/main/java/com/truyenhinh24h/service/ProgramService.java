package com.truyenhinh24h.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truyenhinh24h.dao.ProgramRepository;
import com.truyenhinh24h.model.Program;
import com.truyenhinh24h.model.ProgramDto;

@Service
public class ProgramService {

	@Autowired
	private ProgramRepository programRepository;
	
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;
	
	public ProgramDto createOrUpdate(ProgramDto programDto) { 
		Program program = mapper(programDto);
		Program result = null;
		if(program.getProgramId() == null) {
			program.setProgramId(sequenceGeneratorService.generateSequence(Program.SEQUENCE_NAME));
			result = programRepository.insert(program);
		} else {
			result = programRepository.save(program);
		}
		return mapper(result);
	}
	
	
	Program mapper(ProgramDto programDto) {
		if(programDto == null) {
			return null;
		}
		Program program = new Program();
		program.setCategoryIds(programDto.getCategoryIds());
		program.setDescription(programDto.getDescription());
		program.setLogoUrl(programDto.getLogoUrl());
		program.setName(programDto.getName());
		program.setProgramId(programDto.getProgramId());
		program.setRate(programDto.getRate());
		program.setYear(programDto.getYear());
		return program;
	}
	
	ProgramDto mapper(Program program) {
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
