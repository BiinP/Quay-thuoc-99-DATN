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
import com.qlnt.model.Account;
import com.qlnt.model.Product;
import com.qlnt.service.ProductService;
import com.qlnt.service.UploadService;

@RestController
@RequestMapping("/api/products")
public class ProductAPI {
	@Autowired
	private ProductService productService;
	@Autowired
	private UploadService uploadService;
	
	@GetMapping
	public Page<Product> findAll(@RequestParam("kw") Optional<String> kw,
			@RequestParam("currentPage") Optional<Integer> currentPage){
		return productService.findAllByKeyword(kw, currentPage);
	}
	
	@GetMapping("/{id}")
	public Product findById(@PathVariable("id") Integer id) {
		return productService.findById(id);
	}
	
	@PostMapping
	public ResponseEntity<Product> create(@RequestBody Product product){
		System.out.println(product.getDonViGoc());
		if (productService.existById(product.getId())) {
			return ResponseEntity.badRequest().build();
		}else {
			return ResponseEntity.ok(productService.save(product));
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Product> update(@PathVariable("id") Integer id,
			@RequestBody Product product){
		if (productService.existById(id)) {
			String imgOld = productService.findById(id).getPhoto();
			if(!product.getPhoto().equals(imgOld) && !imgOld.equals("photo.png")) {
				uploadService.delete("product", imgOld);
			}
			return ResponseEntity.ok(productService.save(product));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public JsonNode delete (@PathVariable("id") Integer id){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		if (productService.existById(id)) {
			Product product = productService.findById(id);
			if(productService.existInGoodsOrInputDetail(id)) {
				product.setActive(false);
				productService.save(product);
				node.put("isFound", true);
				node.put("isExist", true);
				return node;
			}else {
				productService.deleteById(id);
				if(!product.getPhoto().equals("photo.png")) {
					uploadService.delete("product", product.getPhoto());
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
