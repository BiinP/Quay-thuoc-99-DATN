package com.qlnt.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.qlnt.model.PromotionDetail;
import com.qlnt.repository.PromotionDetailRepository;
import com.qlnt.service.PromotionDetailService;

@Service
public class PromotionDetailServiceImpl implements PromotionDetailService{
	@Autowired
	private PromotionDetailRepository promoDetailRepo;
	
	@Override
	public PromotionDetail save(PromotionDetail T) {
		return promoDetailRepo.save(T);
	}

	@Override
	public void deleteById(Integer id) {
		promoDetailRepo.deleteById(id);
	}

	@Override
	public Page<PromotionDetail> findAll(Optional<Integer> kw, Optional<Integer> currentPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PromotionDetail findById(Integer id) {
		return promoDetailRepo.findById(id).get();
	}

	@Override
	public Boolean existById(Integer id) {
		return promoDetailRepo.existsById(id);
	}

	@Override
	public List<PromotionDetail> findByPromotionId(String id) {
		return promoDetailRepo.findByPromotionId(id);
	}

	@Override
	public void deleteByPromotionId(String id) {
		// TODO Auto-generated method stub
		promoDetailRepo.deleteByPromotionId(id);
	}

}
