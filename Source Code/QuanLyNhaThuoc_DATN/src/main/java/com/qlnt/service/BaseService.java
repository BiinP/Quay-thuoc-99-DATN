package com.qlnt.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

public interface BaseService <T, ID>{
	T save(T T);
	void deleteById(ID id);
	Page<T> findAll(Optional<ID> kw, Optional<Integer> currentPage);
	T findById(ID id);
	Boolean existById(ID id);
	
}
