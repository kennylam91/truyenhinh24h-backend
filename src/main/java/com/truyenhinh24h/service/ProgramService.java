package com.truyenhinh24h.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.truyenhinh24h.controller.ProgramForm;
import com.truyenhinh24h.dao.CategoryRepository;
import com.truyenhinh24h.dao.ProgramRepository;
import com.truyenhinh24h.model.Category;
import com.truyenhinh24h.model.CategoryDto;
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
	private CategoryService categoryService;

	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	@CachePut(cacheNames = {"programs"}, key = "#result.id")
	@CacheEvict(cacheNames = {"all-programs"}, allEntries = true)
	public ProgramDto createOrUpdate(Program program) {
		Program result = null;
		if (program.getId() == null) {
			program.setId(sequenceGeneratorService.generateSequence(Program.SEQUENCE_NAME));
			result = programRepository.insert(program);
		} else {
			result = programRepository.save(program);
		}
		return result.getDto();
	}

	public void deleteMulti(Long[] ids) {
		programRepository.deleteByIdIn(ids);
	}

	@Cacheable(cacheNames = {"programs"}, key = "#id")
	public ProgramDto findById(Long id) {
		Optional<Program> optional = programRepository.findById(id);
		if (optional.isPresent()) {
			ProgramDto programDto = optional.get().getDto();
			programDto.setCategories(categoryRepository.findByCodeIn(programDto.getCategoryCodes()));
			return programDto;
		} else {
			return null;
		}
	}

	@Cacheable(cacheNames = {"all-programs"})
	public Page<ProgramDto> getAll(Pageable pageable) {
		Page<Program> programPage = programRepository.findAll(pageable);
		List<ProgramDto> programDtoList = Collections.emptyList();
		if (programPage.hasContent()) {
			programDtoList = programPage.getContent().stream().map(Program::getDto).collect(Collectors.toList());
			for (ProgramDto programDto : programDtoList) {
				if (programDto.getCategoryCodes() != null && programDto.getCategoryCodes().length > 0) {
					programDto.setCategories(categoryRepository.findByCodeIn(programDto.getCategoryCodes()));
				}
			}
		}
		return new PageImpl<>(programDtoList, pageable, programPage.getTotalElements());
	}

	public Page<ProgramDto> search(ProgramForm programForm) {
		Sort sort = Sort.by(Sort.Direction.ASC, "name");
		if (programForm.getSortBy() != null) {
			sort = Sort.by(programForm.getSortDirectionObj(), programForm.getSortBy())
					.and(Sort.by("logo").descending());
		}
		Pageable pageable = PageRequest.of(programForm.getPage() - 1, programForm.getLimit(), sort);
		Page<ProgramDto> programDtoPage = null;
		programDtoPage = programRepository.search(programForm, pageable);
		if (programDtoPage == null || !programDtoPage.hasContent()) {
			return new PageImpl<>(Collections.emptyList());
		} else {
			List<ProgramDto> programDtoList = programDtoPage.getContent();
			for (ProgramDto programDto : programDtoList) {
				programDto.setCategories(this.findCategoryList(Arrays.asList(programDto.getCategoryCodes())));
			}
			return new PageImpl<>(programDtoList, pageable, programDtoPage.getTotalElements());
		}
	}
	
	@Cacheable(cacheNames = {"all-programs"}, key = "{#form.getStartTimeFrom(), #form.getStartTimeTo()}")
	public List<ProgramDto> getTodayPrograms(ProgramForm form) {
		form.setPage(1);
		form.setLimit(8);
		form.setSortBy("rank");
		form.setSortDirection("DESC");
		return this.search(form).getContent();
	}
	
	@Cacheable(cacheNames = {"all-programs"}, key = "{#form.getStartTimeFrom(), #form.getStartTimeTo()}")
	public List<ProgramDto> getTomorrowPrograms(ProgramForm form) {
		form.setPage(1);
		form.setLimit(8);
		form.setSortBy("rank");
		form.setSortDirection("DESC");
		return this.search(form).getContent();
	}

	Program mapper(ProgramDto programDto) {
		if (programDto == null) {
			return null;
		}
		Program program = new Program();
		program.setCategoryCodes(programDto.getCategoryCodes());
		program.setDescription(programDto.getDescription());
		program.setLogo(programDto.getLogo());
		program.setName(programDto.getName());
		program.setEnName(programDto.getEnName());
		program.setId(programDto.getId());
		program.setRank(programDto.getRank());
		program.setYear(programDto.getYear());
		program.setTrailer(programDto.getTrailer());
		return program;
	}

	@Transactional
	public void importMulti(List<Program> programs) {
		for (Program program : programs) {
			if (program.getId() == null) {
				program.setId(sequenceGeneratorService.generateSequence(Program.SEQUENCE_NAME));
			}
			programRepository.save(program);
		}

	}

	
	
	private Set<Category> findCategoryList(List<Long> categoryCodes){
		if(categoryCodes.isEmpty()) {
			return Collections.emptySet();
		}else {
			List<CategoryDto> categoryDtoList = categoryService.getAll();
			Set<Category> result = new HashSet<>();
			for (Long code : categoryCodes) {
				CategoryDto found = categoryDtoList.stream()
						.filter(item -> Objects.equals(item.getCode(), code))
						.findFirst().orElse(null);
				if(found != null) {
					result.add(categoryService.mapper(found));
				}
			}
			return result;
			
		}
	}
}
