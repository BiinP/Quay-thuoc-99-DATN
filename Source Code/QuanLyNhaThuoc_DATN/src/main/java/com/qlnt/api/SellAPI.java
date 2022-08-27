package com.qlnt.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.qlnt.model.Account;
import com.qlnt.model.Goods;
import com.qlnt.model.Order;
import com.qlnt.service.AccountService;
import com.qlnt.service.GoodsService;
import com.qlnt.service.OrderService;

@RestController
@RequestMapping("/api/sell")
public class SellAPI {
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/get-token-ghn")
	public Map<String, String> token() {
		Map<String, String> response = new HashMap<String, String>();
		response.put("Token", "b275f22d-2474-11ed-84ac-3acece2f25ab");
		response.put("ShopId", "118832");
		return response;
	}
	@GetMapping("/search")
	public List<Map<String, Object>> search(@RequestParam String kw){
		List<Map<String, Object>> db = goodsService.findGoodsForSearch(kw, 0);
		return db;
	}
	@GetMapping("/goods/{id}")
	public Goods addGoods(@PathVariable("id") Integer id) {
		Goods goods = goodsService.findById(id);
		return goods;
	}
	@GetMapping("/account/search")
	public List<Account> searchCustomer(@RequestParam String kw){
		int a = 5;
		Optional<Integer> b = Optional.of(a);
		return accountService.findAll(Optional.of(kw), Optional.of(0)).getContent();
	}
	@PostMapping
	public Order sell(@RequestBody JsonNode order) {
		return orderService.sellInStore(order);
	}
}
