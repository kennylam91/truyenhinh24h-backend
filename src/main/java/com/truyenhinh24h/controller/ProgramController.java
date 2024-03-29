package com.truyenhinh24h.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truyenhinh24h.common.Utils;
import com.truyenhinh24h.dao.ProgramRepository;
import com.truyenhinh24h.model.AccessLog;
import com.truyenhinh24h.model.Program;
import com.truyenhinh24h.model.ProgramDto;
import com.truyenhinh24h.service.AccessLogService;
import com.truyenhinh24h.service.ProgramService;
import com.truyenhinh24h.utils.Mapper;

@RestController
@RequestMapping(path = "/rest/v1/programs")
public class ProgramController {

	private static final Logger logger = LogManager.getLogger(ProgramController.class);

	@Autowired
	private ProgramService programService;
	@Autowired
	private AccessLogService accessLogService;
	@Autowired
	private ProgramRepository programRepository;
	@Autowired
	private Mapper mapper;

	@PostMapping
	public ResponseEntity<ProgramDto> createOrUpdate(@RequestBody @Valid ProgramForm programForm) {
		logger.info("Create program");
		Program program = mapper.fromTo(programForm, Program.class);
		ProgramDto result = programService.createOrUpdate(program);
		logger.info("Program created: {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping(path = "/delete-multi")
	public ResponseEntity<Void> deleteMulti(@RequestBody ProgramForm programForm) {
		programService.deleteMulti(programForm.getProgramIds());
		logger.info("Deleted Program : {}", Arrays.toString(programForm.getProgramIds()));
		return ResponseEntity.ok().build();
	}

	@GetMapping(path = "/{programId}")
	public ResponseEntity<ProgramDto> getDetail(@PathVariable Long programId, HttpServletRequest request) {
//		AccessLog log = new AccessLog();
//		log.setCreatedAt(new Date());
//		log.setEndPoint("/programs/" + programId);
//		log.setIp(Utils.getClientIpAddress(request));
//		log.setMethod(HttpMethod.GET);
//		accessLogService.createOrUpdate(log);
		ProgramDto programDto = programService.findById(programId);
		return ResponseEntity.ok(programDto);
	}

//	@PostMapping(path = "/today")
//	public ResponseEntity<Page<ProgramDto>> getTodayPrograms(@RequestBody ProgramForm form,
//			HttpServletRequest request) {
//		if (!form.isStartTimeFilterValid()) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//		}
//		AccessLog log = new AccessLog();
//		log.setCreatedAt(new Date());
//		log.setEndPoint("/programs/today");
//		log.setIp(Utils.getClientIpAddress(request));
//		log.setMethod(HttpMethod.POST);
//		accessLogService.createOrUpdate(log);
//		Page<ProgramDto> programDtoPage = programService.search(form);
//		return ResponseEntity.ok(programDtoPage);
//	}

	@PostMapping(path = "/now")
	public ResponseEntity<List<ProgramDto>> getBroadCastingPrograms() {
		return ResponseEntity.ok(programService.getBroadCastingPrograms());
	}

	@PostMapping(path = "/today")
	public ResponseEntity<List<ProgramDto>> getTonightPrograms(@RequestBody ProgramForm form) {
		if (!form.isStartTimeFilterValid()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.ok(programService.findProgramsForClient(form));
	}

	@PostMapping(path = "/tomorrow")
	public ResponseEntity<List<ProgramDto>> getTomorrowPrograms(@RequestBody ProgramForm form,
			HttpServletRequest request) {
		if (!form.isStartTimeFilterValid()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		List<ProgramDto> programDtoPage = programService.findProgramsForClient(form);
		return ResponseEntity.ok(programDtoPage);
	}

	@PostMapping(path = "/get-all")
	public ResponseEntity<Page<ProgramDto>> getAll(@RequestBody ProgramForm programForm) {
		Sort sort = Sort.by(Sort.Direction.ASC, "name");
		Pageable pageable = PageRequest.of(programForm.getPage() - 1, programForm.getLimit(), sort);
		Page<ProgramDto> result = programService.getAll(pageable);
		return ResponseEntity.ok(result);
	}

	@PostMapping(path = "/search")
	public ResponseEntity<Page<ProgramDto>> search(@RequestBody ProgramForm programForm) {
		if (!programForm.isStartTimeFilterValid()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		Page<ProgramDto> programDtoPage = programService.search(programForm);
		return ResponseEntity.ok(programDtoPage);
	}

	@PostMapping("/import")
	public ResponseEntity<Void> importMulti(@Valid @RequestBody List<ProgramForm> forms) {
		List<Program> programs = mapper.fromToList(forms, Program.class);
		programService.importMulti(programs);
		return ResponseEntity.ok().build();
	}

//	@GetMapping(path = "/update-db")
	public void updateDb() {
		List<Program> programs = programRepository.findAll();
		for (Program program : programs) {
			if (!program.getName().contentEquals(program.getEnName())) {
				String onlyTextName = program.getName().replaceAll(Utils.SYMBOL_REGEX, "") + " "
						+ program.getEnName().replaceAll(Utils.SYMBOL_REGEX, "");
				program.setOnlyTextName(onlyTextName);
			} else {
				program.setOnlyTextName(program.getName());
			}

			programRepository.save(program);
		}

	}

}
