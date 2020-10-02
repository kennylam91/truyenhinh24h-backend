package com.truyenhinh24h.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truyenhinh24h.model.CategoryDto;
import com.truyenhinh24h.service.CategoryService;

@RestController
@RequestMapping(path = "/rest/v1/categories")
@CrossOrigin(origins = {"http://localhost:3000", "https://truyenhinh24h.live"})
public class CategoryController {

	private static final Logger logger = LogManager.getLogger(CategoryController.class);
	
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping
	public ResponseEntity<CategoryDto> createOrUpdate(@RequestBody CategoryForm categoryForm){
		logger.info("Create category");
		CategoryDto categoryDto = mapper(categoryForm);
		try {
			CategoryDto result = categoryService.createOrUpdate(categoryDto);
			logger.info("Category created: {}", result);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			logger.error(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping(path = "/get-all")
	public ResponseEntity<List<CategoryDto>> getAll(@RequestBody CategoryForm categoryForm) {
		try {
			List<CategoryDto> result = categoryService.getAll();
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	private CategoryDto mapper(CategoryForm category) {
		if(category == null) {
			return null;
		}
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(category.getId());
		categoryDto.setName(category.getName());
		return categoryDto;
	}
}
