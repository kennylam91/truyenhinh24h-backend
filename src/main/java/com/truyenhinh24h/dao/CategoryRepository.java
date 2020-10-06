package com.truyenhinh24h.dao;

import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.truyenhinh24h.model.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, Long>{

	Set<Category> findByIdIn(Long[] ids);
	
	Set<Category> findByCodeIn(Long[] ids);
}
