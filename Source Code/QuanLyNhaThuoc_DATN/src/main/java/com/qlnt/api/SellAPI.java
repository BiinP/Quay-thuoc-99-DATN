package com.qlnt.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qlnt.model.Goods;
import com.qlnt.service.GoodsService;

@RestController
@RequestMapping("/api/sell")
public class SellAPI {
	@Autowired
	private GoodsService goodsService;
	
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
}
