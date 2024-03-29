package com.truyenhinh24h.controller;

import java.util.List;

import javax.validation.Valid;

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

import com.truyenhinh24h.common.Utils;
import com.truyenhinh24h.model.Category;
import com.truyenhinh24h.model.CategoryDto;
import com.truyenhinh24h.service.CategoryService;

@RestController
@RequestMapping(path = "/rest/v1/categories")
public class CategoryController {

	private static final Logger logger = LogManager.getLogger(CategoryController.class);

	@Autowired
	private CategoryService categoryService;

	@PostMapping
	public ResponseEntity<CategoryDto> createOrUpdate(@RequestBody @Valid CategoryForm categoryForm) {
		logger.info("Create category");
		Category category = mapper(categoryForm);
		CategoryDto result = categoryService.createOrUpdate(category);
		logger.info("Category created: {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping(path = "/get-all")
	public ResponseEntity<List<CategoryDto>> getAll(@RequestBody CategoryForm categoryForm) {
		List<CategoryDto> result = categoryService.getAll();
		return ResponseEntity.ok(result);
	}

	private Category mapper(CategoryForm data) {
		if (data == null) {
			return null;
		}
		Category category = new Category();
		category.setId(data.getId());
		category.setName(data.getName());
		category.setCode(data.getCode());
		return category;
	}
}
