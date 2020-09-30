package com.truyenhinh24h.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truyenhinh24h.dao.CategoryRepository;
import com.truyenhinh24h.model.Category;
import com.truyenhinh24h.model.CategoryDto;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;
	
	public CategoryDto createOrUpdate(CategoryDto categoryDto) {
		Category category = mapper(categoryDto);
		Category result = null;
		if(category.getCategoryId() == null) {
			category.setCategoryId(sequenceGeneratorService.generateSequence(Category.SEQUENCE_NAME));
			result = categoryRepository.insert(category);
		} else {
			result = categoryRepository.save(category);
		}
		return mapper(result);
	}
	
	public List<CategoryDto> getAll(){
		List<Category> categoryList = categoryRepository.findAll();
		if(!categoryList.isEmpty()) {
			return categoryList.stream().map(this::mapper).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}
	
	Category mapper(CategoryDto categoryDto) {
		if(categoryDto == null) {
			return null;
		}
		Category category = new Category();
		category.setCategoryId(categoryDto.getCategoryId());
		category.setName(categoryDto.getName());
		return category;
	}
	
	CategoryDto mapper(Category category) {
		if(category == null) {
			return null;
		}
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setCategoryId(category.getCategoryId());
		categoryDto.setName(category.getName());
		return categoryDto;
	}
}
