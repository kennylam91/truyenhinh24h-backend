package com.truyenhinh24h.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truyenhinh24h.model.Program;
import com.truyenhinh24h.model.ProgramDto;
import com.truyenhinh24h.model.Schedule;
import com.truyenhinh24h.service.ProgramService;

@RestController
@RequestMapping(path = "/rest/v1/programs")
@CrossOrigin(origins = {"http://localhost:3000", "https://truyenhinh24h.live"})
public class ProgramController {
	
	private static final Logger logger = LogManager.getLogger(ProgramController.class);
	
	@Autowired
	private ProgramService programService;
	
	@PostMapping
	public ResponseEntity<ProgramDto> createOrUpdate(@RequestBody @Valid ProgramForm programForm){
		logger.info("Create program");
		Program program = mapper(programForm);
		try {
			ProgramDto result = programService.createOrUpdate(program);
			logger.info("Program created: {}", result);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			logger.error(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping(path="/delete-multi")
	public ResponseEntity<Void> deleteMulti(@RequestBody ProgramForm programForm) {
		try {
			programService.deleteMulti(programForm.getProgramIds());
			logger.info("Deleted Program : {}", Arrays.toString(programForm.getProgramIds()));
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping(path = "/{programId}")
	public ResponseEntity<ProgramDto> getDetail(@PathVariable Long programId){
		try {
			ProgramDto programDto = programService.findById(programId);
			return ResponseEntity.ok(programDto);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping(path = "/get-all")
	public ResponseEntity<Page<ProgramDto>> getAll(@RequestBody ProgramForm programForm) {
		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "name");
			Pageable pageable = PageRequest.of(programForm.getPage() - 1, programForm.getLimit(), sort);
			Page<ProgramDto> result = programService.getAll(pageable);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping(path = "/search")
	public ResponseEntity<Page<ProgramDto>> search(@RequestBody ProgramForm programForm) {
		try {
			Page<ProgramDto> programDtoPage = programService.search(programForm);
			return ResponseEntity.ok(programDtoPage);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping("/import")
	public ResponseEntity<Void> importMulti(@Valid @RequestBody List<ProgramForm> forms){
		try {
			List<Program> programs = forms.stream().map(this::mapper).collect(Collectors.toList());
			programService.importMulti(programs);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	private Program mapper(ProgramForm data) {
		if(data == null) {
			return null;
		}
		
		if(data.getCategories() != null) {
			data.setCategoryCodes(data.getCategories());
		}
		
		Program program = new Program();
		program.setCategoryCodes(data.getCategoryCodes());
		program.setDescription(data.getDescription());
		program.setLogo(data.getLogo());
		program.setName(data.getName());
		program.setEnName(data.getEnName());
		program.setId(data.getId());
		program.setRank(data.getRank() != null ? data.getRank() : 1L);
		program.setYear(data.getYear());
		program.setTrailer(data.getTrailer());
		
		return program;
	}

}
