package com.qlnt.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qlnt.model.Promotion;
import com.qlnt.repository.PromotionRepository;
import com.qlnt.service.PromotionService;

@Service
public class PromotionServiceImpl implements PromotionService {
	@Autowired
	private PromotionRepository promoRepo;

	@Override
	public Promotion save(Promotion T) {
		return promoRepo.save(T);
	}

	@Override
	public void deleteById(String id) {
		promoRepo.deleteById(id);
	}

	@Override
	public Page<Promotion> findAll(Optional<String> kw, Optional<Integer> currentPage) {
		String keyword = kw.orElse("");
		Pageable page = PageRequest.of(currentPage.orElse(0), 10);
		return promoRepo.findAllByKeyword("%" + keyword + "%", page);
	}

	@Override
	public Promotion findById(String id) {
		return promoRepo.findById(id).get();
	}

	@Override
	public Boolean existById(String id) {
		return promoRepo.existsById(id);
	}

	@Override
	public List<Promotion> findPromotionAvailble() {
		return promoRepo.findPromotionAvailble();
	}

}
