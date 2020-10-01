package com.truyenhinh24h.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.truyenhinh24h.controller.ProgramForm;
import com.truyenhinh24h.dao.CategoryRepository;
import com.truyenhinh24h.dao.ProgramRepository;
import com.truyenhinh24h.model.Program;
import com.truyenhinh24h.model.ProgramDto;
import com.truyenhinh24h.model.Schedule;
import com.truyenhinh24h.model.ScheduleDto;

@Service
public class ProgramService {
	
	private static final Logger logger = LogManager.getLogger(ProgramService.class);


	@Autowired
	private ProgramRepository programRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
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
			ProgramDto programDto = mapper(optional.get());
			programDto.setCategories(categoryRepository.findByCategoryIdIn(programDto.getCategoryIds()));
			return programDto;
		} else {
			return null;
		}
	}
	
	public Page<ProgramDto> getAll(Pageable pageable) {
		Page<Program> programPage = programRepository.findAll(pageable);
		List<ProgramDto> programDtoList = null;
		if (programPage.hasContent()) {
			programDtoList = programPage.getContent().stream().map(this::mapper).collect(Collectors.toList());
			for (ProgramDto programDto : programDtoList) {
				if(programDto.getCategoryIds() != null && programDto.getCategoryIds().length > 0) {
					programDto.setCategories(categoryRepository.findByCategoryIdIn(programDto.getCategoryIds()));
				}
			}
		}
		Page<ProgramDto> programDtoPage = new PageImpl<ProgramDto>(programDtoList, pageable,
				programPage.getTotalElements());
		return programDtoPage;
	}
	
	public Page<ProgramDto> search(ProgramForm programForm){
		Pageable pageable = PageRequest.of(programForm.getPage() - 1, programForm.getLimit(), 
				Sort.by(Sort.Direction.ASC, "name"));
		Page<Program> programPage = null;
		try {
			programPage = programRepository.search(programForm, pageable);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		if (programPage == null || !programPage.hasContent()) {
			return new PageImpl<>(Collections.emptyList(), pageable, 0);
		} else {
			List<Program> programList = programPage.getContent();
			List<ProgramDto> scheduleDtoList = programList.stream().map(this::mapper).collect(Collectors.toList());
			return new PageImpl<>(scheduleDtoList, pageable, programPage.getTotalElements());
		}
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
		program.setEnName(programDto.getEnName());
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
		programDto.setEnName(program.getEnName());
		programDto.setProgramId(program.getProgramId());
		programDto.setRate(program.getRate());
		programDto.setYear(program.getYear());
		
		return programDto;
		
	}
}
