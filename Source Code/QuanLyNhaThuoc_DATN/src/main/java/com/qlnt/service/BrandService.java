package com.qlnt.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qlnt.model.Brand;

public interface BrandService extends BaseService<Brand, String>{
	Boolean existInProduct (String id);
	List<Brand> findAll();
	Map<String, Object> findBrandForListBrand(Integer page);
}
