package com.qlnt.service;

import java.util.List;

import com.qlnt.model.Category;

public interface CategoryService extends BaseService<Category, String>{
	Boolean existInSubCategory(String id);
	List<Category> findAll();
}
