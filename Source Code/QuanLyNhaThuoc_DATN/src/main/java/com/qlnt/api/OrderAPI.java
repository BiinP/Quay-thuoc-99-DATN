package com.qlnt.api;

import java.security.cert.PKIXRevocationChecker.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qlnt.model.Order;
import com.qlnt.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderAPI {
	@Autowired
	private OrderService orderService;
//	@Autowired
//	private OrderDetail orderService;
	
	@GetMapping
	public Page<Order> getAll(@RequestParam Optional<Integer> kw, 
			@RequestParam Optional<Integer> currentPage){
		return orderService.findAll(kw, currentPage);
	}
//	@GetMapping("/doi-xac-nhan")
//	public Page<Order> DonHangDoiXacNhan(@RequestParam Optional<Integer> kw, 
//			@RequestParam Optional<Integer> currentPage){
//		
//	}
	@GetMapping("/detail/{id}")
	public Map<String, Object> getDetail(@PathVariable Integer id){
		return orderService.findDetailForOrderMgmt(id);
	}
}
