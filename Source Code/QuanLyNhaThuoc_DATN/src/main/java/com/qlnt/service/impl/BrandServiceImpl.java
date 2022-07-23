package com.qlnt.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qlnt.model.Brand;
import com.qlnt.repository.BrandRepository;
import com.qlnt.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService{
	@Autowired private BrandRepository brandRepository;

	@Override
	public Brand save(Brand T) {
		return brandRepository.save(T);
	}

	@Override
	public void deleteById(String id) {
		brandRepository.deleteById(id);
	}

	@Override
	public Page<Brand> findAll(Optional<String> kw, Optional<Integer> currentPage) {
		String keyword = kw.orElse("");
		Pageable page = PageRequest.of(currentPage.orElse(0), 10);
		return brandRepository.findAll("%"+keyword+"%", page);
	}

	@Override
	public Brand findById(String id) {
		return brandRepository.findById(id).get();
	}

	@Override
	public Boolean existById(String id) {
		return brandRepository.existsById(id);
	}

	@Override
	public Boolean existInProduct(String id) {
		Integer existInProduct = brandRepository.existInProduct(id).orElse(0);
		if(existInProduct >= 1) {
			return true;
		}
		return false;
	}
	
	
}
