package com.qlnt.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qlnt.model.Product;
import com.qlnt.model.Util;
import com.qlnt.service.UtilService;

@RestController
@RequestMapping("/api/utils")
public class UtilAPI {
	@Autowired 
	private UtilService utilService;
	
	@GetMapping
	public Page<Util> findAll(@RequestParam("kw") Optional<String> kw,
			@RequestParam("currentPage") Optional<Integer> currentPage){
		return utilService.findAll(kw, currentPage);
	}
	@GetMapping("/{id}")
	public Util findById(@PathVariable("id") String id) {
		return utilService.findById(id);
	}
	@PostMapping
	public ResponseEntity<Util> create(@RequestBody Util util){
		if (utilService.existById(util.getId())) {
			return ResponseEntity.badRequest().build();
		}else {
			return ResponseEntity.ok(utilService.save(util));
		}
	}
	@PutMapping("/{id}")
	public ResponseEntity<Util> update(@PathVariable("id") String id,
			@RequestBody Util util){
		if (utilService.existById(id)) {
			return ResponseEntity.ok(utilService.save(util));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	@DeleteMapping("/{id}")
	public JsonNode delete (@PathVariable("id") String id){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		if (utilService.existById(id)) {
			Util util = utilService.findById(id);
			if(utilService.existInGoods(id)) {
				util.setActive(false);
				utilService.save(util);
				node.put("isFound", true);
				node.put("isExist", true);
				return node;
			}else {
				utilService.deleteById(id);
				node.put("isFound", true);
				node.put("isExist", false);
				return node;
			}
		}else {
			node.put("isFound", false);
			node.put("isExist", false);
			return node;
		}
	}
}
