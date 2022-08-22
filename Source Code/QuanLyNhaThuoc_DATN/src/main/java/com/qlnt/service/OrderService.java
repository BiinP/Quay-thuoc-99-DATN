package com.qlnt.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.databind.JsonNode;
import com.qlnt.model.Order;

public interface OrderService extends BaseService<Order, Integer>{
	Order create(JsonNode orderData);
	List<Order> findByAccount(String email);
	Map<String, Object> findDetailForOrderMgmt(Integer id);
	Order confirmOrder(Integer id);
	Order successOrder(Integer id);
	Order cancelOrder(Integer id);
//	Page<Order> findForDoiXacNhan(Optional<Integer> )
}
