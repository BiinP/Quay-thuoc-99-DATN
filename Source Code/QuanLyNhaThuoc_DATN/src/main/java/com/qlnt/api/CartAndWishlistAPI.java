package com.qlnt.api;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.qlnt.model.Favorite;
import com.qlnt.model.Goods;
import com.qlnt.model.Order;
import com.qlnt.service.FavoriteService;
import com.qlnt.service.GoodsService;
import com.qlnt.service.OrderService;
import com.qlnt.service.ProductService;

@RestController
public class CartAndWishlistAPI {
	@Autowired
	private ProductService productService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private FavoriteService favoriteService;
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/cart/goods/{id}")
	public Goods addGoods(@PathVariable("id") Integer id) {
		Goods goods = goodsService.findById(id);
		return goods;
	}
	
	@PostMapping("/wishList/{id}")
	public Map<String, Boolean> like(@PathVariable("id") Integer id,
			Principal principal) {
		Map<String, Boolean> favoriteSave = favoriteService.like(id, principal);
		return favoriteSave;
	}
	
	@PostMapping("/cart/order/purchase")
	public Order purchase(@RequestBody JsonNode order) {
		return orderService.create(order);
	}
}
