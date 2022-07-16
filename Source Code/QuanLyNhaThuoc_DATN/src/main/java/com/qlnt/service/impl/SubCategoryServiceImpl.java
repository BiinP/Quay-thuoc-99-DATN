package com.qlnt.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.qlnt.model.SubCategory;
import com.qlnt.repository.SubCategoryRepository;
import com.qlnt.service.SubCategoryService;

public class SubCategoryServiceImpl implements SubCategoryService{
	@Autowired SubCategoryRepository subCateRepo;
	
	@Override
	public SubCategory save(SubCategory T) {
		return subCateRepo.save(T);
	}

	@Override
	public void deleteById(String id) {
		subCateRepo.deleteById(id);
	}

	@Override
	public Page<SubCategory> findAll(Optional<String> kw, Optional<Integer> currentPage) {
		String keyword = kw.orElse("");
		Pageable page = PageRequest.of(currentPage.orElse(0), 10);
		return subCateRepo.findAll("%"+keyword+"%", page);
	}

	@Override
	public SubCategory findById(String id) {
		return subCateRepo.findById(id).get();
	}

	@Override
	public Boolean existById(String id) {
		return subCateRepo.existsById(id);
	}

}
