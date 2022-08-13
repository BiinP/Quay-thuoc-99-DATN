package com.qlnt.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qlnt.model.Goods;
import com.qlnt.model.InputDetail;
import com.qlnt.model.Order;
import com.qlnt.model.OrderDetail;
import com.qlnt.model.Product;
import com.qlnt.repository.OrderDetailRepository;
import com.qlnt.repository.OrderRepository;
import com.qlnt.repository.ProductRepository;
import com.qlnt.service.GoodsService;
import com.qlnt.service.InputDetailService;
import com.qlnt.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private OrderDetailRepository orderDetailRepo;
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private InputDetailService inputDetailService;
	
	@Override
	public Order create(JsonNode orderData) {
		ObjectMapper mapper = new ObjectMapper();
		Order order = mapper.convertValue(orderData, Order.class);
		Order orderSaved = orderRepo.save(order);
		TypeReference<List<OrderDetail>> type = new TypeReference<List<OrderDetail>>() {
		};
		List<OrderDetail> orderDetails = mapper.convertValue(orderData.get("orderDetails"), type);
		for(OrderDetail o : orderDetails) {
			o.setOrder(orderSaved);
			Goods goods = goodsService.findById(o.getGoodsId());
			Product product = goods.getProduct();
			
			product.setTonKho(product.getTonKho()- (o.getSoLuong() / goods.getQuiDoi()));
			productRepo.save(product);
			
			Integer id = product.getId();
			InputDetail inputDetail = inputDetailService.findByProductId(id).get(0);
			o.setInputDetail(inputDetail);
		}
		
		orderDetailRepo.saveAll(orderDetails);
		return orderSaved;
	}

	@Override
	public Order findById(Integer id) {
		// TODO Auto-generated method stub
		return orderRepo.findById(id).get();
	}

	@Override
	public List<Order> findByAccount(String email) {
		// TODO Auto-generated method stub
		return orderRepo.findByAccount(email);
	}

	@Override
	public Order save(Order T) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Page<Order> findAll(Optional<Integer> kw, Optional<Integer> currentPage) {
		Pageable pageable = PageRequest.of(currentPage.orElse(0), 10);
		Integer id = kw.orElse(null);
		return orderRepo.findAllForOrderMgmt(id, pageable);
	}

	@Override
	public Boolean existById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> findDetailForOrderMgmt(Integer id) {
		Map<String, Object> db = new HashMap<String, Object>();
		Order order = this.findById(id);
		db.put("order", order);
		List<OrderDetail> lstOrderDetail = order.getOrderDetails();
		List<Map<String, Object>> orderDetails = new ArrayList<Map<String,Object>>();
		for(OrderDetail od : lstOrderDetail) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("goodsId", od.getGoodsId());
			Product product = od.getInputDetail().getProduct();
			map.put("name", product.getName());
			map.put("donGia", od.getDonGia());
			map.put("giamGia", od.getGiamGia());
			map.put("soLuong", od.getSoLuong());
			Float tongTien =  (od.getDonGia() - od.getGiamGia()) * od.getSoLuong();
			map.put("tongTien", tongTien);
			orderDetails.add(map);
		}
		db.put("orderDetails", orderDetails);
		return db;
	}

}
