package com.truyenhinh24h.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.truyenhinh24h.controller.ProgramForm;
import com.truyenhinh24h.model.Program;
import com.truyenhinh24h.model.ProgramDto;

public interface ProgramRepositoryCustom {

	Page<ProgramDto> search(ProgramForm programForm, Pageable pageable);
}
