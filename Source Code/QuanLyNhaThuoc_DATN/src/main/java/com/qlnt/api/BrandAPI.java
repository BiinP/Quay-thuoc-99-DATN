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
import com.qlnt.model.Brand;
import com.qlnt.model.Brand;
import com.qlnt.service.BrandService;
import com.qlnt.service.UploadService;

@RestController
@RequestMapping("/api/brands")
public class BrandAPI {
	@Autowired private BrandService brandService;
	@Autowired private UploadService uploadService;
	
	@GetMapping
	public Page<Brand> getAll(@RequestParam("kw") Optional<String> kw, 
			@RequestParam("currentPage") Optional<Integer> currentPage){
		return brandService.findAll(kw, currentPage);
	}
	@GetMapping("/all")
	public List<Brand> getAll(){
		return brandService.findAll();
	}
	@GetMapping("/{id}")
	public ResponseEntity<Brand> getById(@PathVariable("id") String id) {
		if(brandService.existById(id)) {
			return ResponseEntity.ok(brandService.findById(id));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	@PostMapping
	public ResponseEntity<Brand> create(@RequestBody Brand brand){
		if(brandService.existById(brand.getId())) {
			return ResponseEntity.badRequest().build();
		}else {
			return ResponseEntity.ok(brandService.save(brand));
		}
	}
	@PutMapping("/{id}")
	public ResponseEntity<Brand> update(@PathVariable("id") String id, @RequestBody Brand brand){
		if (brandService.existById(id)) {
			String imgOld = brandService.findById(id).getPhoto();
			if(!brand.getPhoto().equals(imgOld) && !imgOld.equals("photo.png")) {
				uploadService.delete("brand", imgOld);
			}
			return ResponseEntity.ok(brandService.save(brand));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	@DeleteMapping("/{id}")
	public JsonNode delete(@PathVariable("id") String id){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		if (brandService.existById(id)) {
			Brand brand = brandService.findById(id);
			if(brandService.existInProduct(id)) {
				brand.setActive(false);
				brandService.save(brand);
				node.put("isFound", true);
				node.put("isExist", true);
				return node;
			}else {
				brandService.deleteById(id);
				if(!brand.getPhoto().equals("photo.png")) {
					uploadService.delete("brand", brand.getPhoto());
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
