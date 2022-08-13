package com.qlnt.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qlnt.model.Category;
import com.qlnt.repository.CategoryRepository;
import com.qlnt.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{
	@Autowired private CategoryRepository cateRepo;
	
	@Override
	public Category save(Category T) {
		return cateRepo.save(T);
	}

	@Override
	public void deleteById(String id) {
		cateRepo.deleteById(id);
	}

	@Override
	public Page<Category> findAll(Optional<String> kw, Optional<Integer> currentPage) {
		String keyword = kw.orElse("");
		Pageable page = PageRequest.of(currentPage.orElse(0), 10);
		return cateRepo.findAll("%"+keyword+"%", page);
	}

	@Override
	public Category findById(String id) {
		return cateRepo.findById(id).get();
	}

	@Override
	public Boolean existById(String id) {
		return cateRepo.existsById(id);
	}

	@Override
	public Boolean existInSubCategory(String id) {
		Integer existInSubCategory = cateRepo.existInSubCategory(id).orElse(0);
		if(existInSubCategory >= 1) {
			return true;
		}
		return false;
	}

	@Override
	public List<Category> findAll() {
		// TODO Auto-generated method stub
		return cateRepo.findAll();
	}
	
}
