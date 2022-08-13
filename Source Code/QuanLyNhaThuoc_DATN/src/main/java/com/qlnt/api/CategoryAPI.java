package com.qlnt.api;

import java.util.List;
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
import com.qlnt.model.Category;
import com.qlnt.service.CategoryService;
import com.qlnt.service.UploadService;

@RestController
@RequestMapping("/api/categories")
public class CategoryAPI {
	@Autowired private CategoryService cateService;

	@GetMapping
	public Page<Category> getAll(@RequestParam("kw") Optional<String> kw,
			@RequestParam("currentPage") Optional<Integer> currentPage) {
		return cateService.findAll(kw, currentPage);
	}
	@GetMapping("/all")
	public List<Category> getAll(){
		return cateService.findAll();
	}

	@GetMapping("/{id}")
	public Category getById(@PathVariable("id") String id) {
		return cateService.findById(id);
	}

	@PostMapping
	public ResponseEntity<Category> create(@RequestBody Category category) {
		if (cateService.existById(category.getId())) {
			return ResponseEntity.badRequest().build();
		}else {
			return ResponseEntity.ok(cateService.save(category));
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Category> update(@PathVariable("id") String id, @RequestBody Category category){
		if (cateService.existById(id)) {
			return ResponseEntity.ok(cateService.save(category));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public JsonNode delete(@PathVariable("id") String id){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		if (cateService.existById(id)) {
			Category category = cateService.findById(id);
			if(cateService.existInSubCategory(id)) {
				category.setActive(false);
				cateService.save(category);
				node.put("isFound", true);
				node.put("isExist", true);
				return node;
			}else {
				cateService.deleteById(id);
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
