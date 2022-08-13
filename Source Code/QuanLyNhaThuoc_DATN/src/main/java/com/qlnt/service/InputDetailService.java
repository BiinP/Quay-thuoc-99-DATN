package com.qlnt.service;

import java.util.List;

import com.qlnt.model.InputDetail;

public interface InputDetailService extends BaseService<InputDetail, Integer>{
	List<InputDetail> findAllByInputId(Integer id);
	List<InputDetail> findByProductId(Integer id);
//	Boolean existInOrderOrReturn(Integer id);
}
