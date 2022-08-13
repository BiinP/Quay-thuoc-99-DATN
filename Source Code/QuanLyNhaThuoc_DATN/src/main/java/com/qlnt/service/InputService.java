package com.qlnt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.qlnt.model.Input;
import com.qlnt.model.Product;

public interface InputService extends BaseService<Input, Integer>{
//	Boolean existInputDetail(Integer id);
	Page<Input> findAllByKeyword(Optional<String> kw,  Optional<Integer> currentPage);
	List<Input> findAll();
}
