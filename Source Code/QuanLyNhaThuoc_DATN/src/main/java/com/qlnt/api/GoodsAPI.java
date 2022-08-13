package com.qlnt.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qlnt.model.Goods;
import com.qlnt.model.Product;
import com.qlnt.service.GoodsService;

@RestController
@RequestMapping("/api/goods")
public class GoodsAPI {
	@Autowired
	private GoodsService goodsService;
	
	@GetMapping("/{id}")
	public Goods findById(@PathVariable("id") Integer id) {
		return goodsService.findById(id);
	}
	@PostMapping
	public ResponseEntity<Goods> create(@RequestBody Goods goods){
		if (goodsService.existById(goods.getId())) {
			return ResponseEntity.badRequest().build();
		}else {
			return ResponseEntity.ok(goodsService.save(goods));
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Goods> update(@PathVariable("id") Integer id,
			@RequestBody Goods goods){
		if (goodsService.existById(id)) {
			return ResponseEntity.ok(goodsService.save(goods));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
		if (goodsService.existById(id)) {
			goodsService.deleteById(id);
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.notFound().build();
		}
	}
}
