package com.qlnt.service;

import java.util.List;

import com.qlnt.model.PromotionDetail;

public interface PromotionDetailService extends BaseService<PromotionDetail, Integer>{
	List<PromotionDetail> findByPromotionId(String id);
	void deleteByPromotionId(String id);
}
