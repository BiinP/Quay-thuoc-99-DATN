package com.qlnt.service;

import java.util.List;

import com.qlnt.model.Promotion;

public interface PromotionService extends BaseService<Promotion, String>{
	List<Promotion> findPromotionAvailble();
}
