package com.qlnt.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qlnt.model.Product;
import com.qlnt.repository.ProductRepository;
import com.qlnt.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
	@Autowired 
	private ProductRepository productRepo;
	
	@Override
	public Product save(Product T) {
		// TODO Auto-generated method stub
		return productRepo.save(T);
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		productRepo.deleteById(id);
	}

	@Override
	public Page<Product> findAll(Optional<Integer> kw, Optional<Integer> currentPage) {
		return null;
	}
	
	@Override
	public Page<Product> findAllByKeyword(Optional<String> kw, Optional<Integer> currentPage) {
		String keyword = kw.orElse("");
		Pageable page = PageRequest.of(currentPage.orElse(0), 10);
		try {
			Integer id = Integer.valueOf(keyword);
			return productRepo.findAllById("%"+id+"%", page);
		} catch (Exception e) {
			return productRepo.findAllByName("%"+keyword+"%", page);
		}
	}

	@Override
	public Product findById(Integer id) {
		return productRepo.findById(id).get();
	}

	@Override
	public Boolean existById(Integer id) {
		// TODO Auto-generated method stub
		return productRepo.existsById(id);
	}

	@Override
	public Boolean existInGoodsOrInputDetail(Integer id) {
		Integer existInGoods = productRepo.existInGoods(id).orElse(0);
		Integer existInInputDetail = productRepo.existInInputDetail(id).orElse(0);
		if(existInGoods >= 1 || existInInputDetail >= 1) {
			return true;
		}
		return false;
	}

}
