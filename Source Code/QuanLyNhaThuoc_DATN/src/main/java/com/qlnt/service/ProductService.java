package com.qlnt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.qlnt.model.Product;

public interface ProductService extends BaseService<Product, Integer>{
	Boolean existInputDetail(Integer id);
	Page<Product> findAllByKeyword(Optional<String> kw,  Optional<Integer> currentPage);
	List<Product> findAll();
	List<Product> searchProductInClient();
}
