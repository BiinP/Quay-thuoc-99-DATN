package com.qlnt.service;

import java.util.List;

import com.qlnt.model.SubCategory;

public interface SubCategoryService extends BaseService<SubCategory, String>{
	Boolean existInProduct(String id);
	List<SubCategory> findAll();
	List<SubCategory> findByCategoryId(String id);
}
