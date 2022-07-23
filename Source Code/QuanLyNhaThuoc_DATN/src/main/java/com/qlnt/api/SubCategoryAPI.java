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
import com.qlnt.model.SubCategory;
import com.qlnt.service.SubCategoryService;
import com.qlnt.service.UploadService;

@RestController
@RequestMapping("/api/sub-categories")
public class SubCategoryAPI {
	@Autowired private SubCategoryService subCateService;
	@Autowired private UploadService uploadService;
	
	@GetMapping
	public Page<SubCategory> getAll(@RequestParam("kw") Optional<String> kw,
			@RequestParam("currentPage") Optional<Integer> currentPage) {
		return subCateService.findAll(kw, currentPage);
	}

	@GetMapping("/{id}")
	public SubCategory getById(@PathVariable("id") String id) {
		return subCateService.findById(id);
	}

	@PostMapping
	public ResponseEntity<SubCategory> create(@RequestBody SubCategory subCategory) {
		if (subCateService.existById(subCategory.getId())) {
			return ResponseEntity.badRequest().build();
		}else {
			return ResponseEntity.ok(subCateService.save(subCategory));
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<SubCategory> update(@PathVariable("id") String id, @RequestBody SubCategory subCategory){
		if (subCateService.existById(id)) {
			String imgOld = subCateService.findById(id).getPhoto();
			if(!subCategory.getPhoto().equals(imgOld) && !imgOld.equals("photo.png")) {
				uploadService.delete("sub-category", imgOld);
			}
			return ResponseEntity.ok(subCateService.save(subCategory));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public JsonNode delete(@PathVariable("id") String id){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		if (subCateService.existById(id)) {
			SubCategory subCategory = subCateService.findById(id);
			if(subCateService.existInProduct(id)) {
				subCategory.setActive(false);
				subCateService.save(subCategory);
				node.put("isFound", true);
				node.put("isExist", true);
				return node;
			}else {
				subCateService.deleteById(id);
				if(!subCategory.getPhoto().equals("photo.png")) {
					uploadService.delete("sub-category", subCategory.getPhoto());
				}
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
