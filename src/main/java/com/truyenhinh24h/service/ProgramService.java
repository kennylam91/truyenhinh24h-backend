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

@Service
public class ProgramService {
	
	private static final Logger logger = LogManager.getLogger(ProgramService.class);


	@Autowired
	private ProgramRepository programRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;
	
	public ProgramDto createOrUpdate(Program program) { 
		Program result = null;
		if(program.getId() == null) {
			program.setId(sequenceGeneratorService.generateSequence(Program.SEQUENCE_NAME));
			result = programRepository.insert(program);
		} else {
			result = programRepository.save(program);
		}
		return mapper(result);
	}
	
	public void deleteMulti(Long[] ids) {
		programRepository.deleteByIdIn(ids);
	}
	
	public ProgramDto findById(Long id) {
		Optional<Program> optional = programRepository.findById(id);
		if (optional.isPresent()) {
			ProgramDto programDto = mapper(optional.get());
			programDto.setCategories(categoryRepository.findByIdIn(programDto.getCategoryIds()));
			return programDto;
		} else {
			return null;
		}
	}
	
	public Page<ProgramDto> getAll(Pageable pageable) {
		Page<Program> programPage = programRepository.findAll(pageable);
		List<ProgramDto> programDtoList = Collections.emptyList();
		if (programPage.hasContent()) {
			programDtoList = programPage.getContent().stream().map(this::mapper).collect(Collectors.toList());
			for (ProgramDto programDto : programDtoList) {
				if(programDto.getCategoryIds() != null && programDto.getCategoryIds().length > 0) {
					programDto.setCategories(categoryRepository.findByIdIn(programDto.getCategoryIds()));
				}
			}
		}
		return new PageImpl<>(programDtoList, pageable,
				programPage.getTotalElements());
	}
	
	public Page<ProgramDto> search(ProgramForm programForm){
		Sort sort = Sort.by(Sort.Direction.ASC, "name");
		if(programForm.getSortBy() != null) {
			sort = Sort.by(programForm.getSortDirectionObj(), programForm.getSortBy());
		}
		Pageable pageable = PageRequest.of(programForm.getPage() - 1, programForm.getLimit(), sort);
		Page<Program> programPage = null;
		try {
			programPage = programRepository.search(programForm, pageable);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		if (programPage == null || !programPage.hasContent()) {
			return new PageImpl<>(Collections.emptyList());
		} else {
			List<Program> programList = programPage.getContent();
			List<ProgramDto> scheduleDtoList = programList.stream()
					.map(this::mapper).collect(Collectors.toList())
					;
			for (ProgramDto programDto : scheduleDtoList) {
				programDto.setCategories(categoryRepository.findByIdIn(programDto.getCategoryIds()));
			}
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
		program.setId(programDto.getId());
		program.setRank(programDto.getRank());
		program.setYear(programDto.getYear());
		program.setTrailerUrl(programDto.getTrailerUrl());
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
		programDto.setId(program.getId());
		programDto.setRank(program.getRank());
		programDto.setYear(program.getYear());
		programDto.setTrailerUrl(program.getTrailerUrl());
		
		return programDto;
		
	}
}
