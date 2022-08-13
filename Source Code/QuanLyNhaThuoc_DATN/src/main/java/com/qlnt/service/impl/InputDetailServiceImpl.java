package com.qlnt.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.qlnt.model.InputDetail;
import com.qlnt.model.Product;
import com.qlnt.repository.InputDetailRepository;
import com.qlnt.service.InputDetailService;
import com.qlnt.service.ProductService;

@Service
public class InputDetailServiceImpl implements InputDetailService{
	@Autowired
	private InputDetailRepository inputDetailRepo;
	@Autowired
	private ProductService productService;
	
	@Override
	public InputDetail save(InputDetail T) {
		// TODO Auto-generated method stub
		Integer productId = T.getProduct().getId();
		Float soLuong = T.getSoLuong();
		Product p = productService.findById(productId);
		Float tonKho = p.getTonKho() + soLuong;
		p.setTonKho(tonKho);
		productService.save(p);
		return inputDetailRepo.save(T);
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Page<InputDetail> findAll(Optional<Integer> kw, Optional<Integer> currentPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputDetail findById(Integer id) {
		// TODO Auto-generated method stub
		return inputDetailRepo.findById(id).get();
	}

	@Override
	public Boolean existById(Integer id) {
		// TODO Auto-generated method stub
		return inputDetailRepo.existsById(id);
	}

	@Override
	public List<InputDetail> findAllByInputId(Integer id) {
		// TODO Auto-generated method stub
		return inputDetailRepo.findAllByInputId(id);
	}

	@Override
	public List<InputDetail> findByProductId(Integer id) {
		return inputDetailRepo.findByProductId(id);
	}

//	@Override
//	public Boolean existInOrderOrReturn(Integer id) {
//		Integer existInOrder = inputDetailRepo.existInOrderDetail(id).orElse(0);
//		Integer existInReturn = inputDetailRepo.existInOrderDetail(id).orElse(0);
//		if(existInGoods >= 1) return true;
//		return false;
//	}

}
