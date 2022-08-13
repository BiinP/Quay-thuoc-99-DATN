package com.qlnt.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qlnt.model.Util;
import com.qlnt.repository.UtilRepository;
import com.qlnt.service.UtilService;

@Service
public class UtilServiceImpl implements UtilService{
	@Autowired private UtilRepository utilRepo;
	
	@Override
	public Util save(Util T) {
		// TODO Auto-generated method stub
		return utilRepo.save(T);
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		utilRepo.deleteById(id);
	}

	@Override
	public Page<Util> findAll(Optional<String> kw, Optional<Integer> currentPage) {
		String keyword = kw.orElse("");
		Pageable page = PageRequest.of(currentPage.orElse(0), 10);
		return utilRepo.findAll("%"+keyword+"%", page);
	}

	@Override
	public Util findById(String id) {
		// TODO Auto-generated method stub
		return utilRepo.findById(id).get();
	}

	@Override
	public Boolean existById(String id) {
		// TODO Auto-generated method stub
		return utilRepo.existsById(id);
	}

	@Override
	public Boolean existInGoods(String id) {
		Integer existInGoods = utilRepo.existInGoods(id).orElse(0);
		if(existInGoods >= 1) return true;
		return false;
	}

}
