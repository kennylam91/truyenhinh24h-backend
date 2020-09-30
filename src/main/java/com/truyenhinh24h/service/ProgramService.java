package com.truyenhinh24h.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
	
	public void deleteMulti(Long[] ids) {
		programRepository.deleteByProgramIdIn(ids);
	}
	
	public ProgramDto findById(Long id) {
		Optional<Program> optional = programRepository.findById(id);
		if (optional.isPresent()) {
			return mapper(optional.get());
		} else {
			return null;
		}
	}
	
	public Page<ProgramDto> getAll(Pageable pageable){
		Page<Program> programPage = programRepository.findAll(pageable);
		List<ProgramDto> programDtoList = null;
		if(programPage.hasContent()) {
			programDtoList = programPage.getContent().stream()
					.map(this::mapper)
					.collect(Collectors.toList());
		}
		Page<ProgramDto> programDtoPage = new PageImpl<ProgramDto>(programDtoList, pageable, 
				programPage.getTotalElements());
		return programDtoPage;
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
