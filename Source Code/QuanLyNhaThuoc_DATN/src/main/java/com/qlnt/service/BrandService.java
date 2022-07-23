package com.qlnt.service;

import com.qlnt.model.Brand;

public interface BrandService extends BaseService<Brand, String>{
	Boolean existInProduct (String id);
}
