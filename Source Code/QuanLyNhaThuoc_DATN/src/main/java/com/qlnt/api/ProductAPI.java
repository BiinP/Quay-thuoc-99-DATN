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
import com.qlnt.model.Account;
import com.qlnt.model.Goods;
import com.qlnt.model.Product;
import com.qlnt.model.PromotionDetail;
import com.qlnt.service.GoodsService;
import com.qlnt.service.ProductService;
import com.qlnt.service.UploadService;

@RestController
@RequestMapping("/api/products")
public class ProductAPI {
	@Autowired
	private ProductService productService;
	@Autowired
	private UploadService uploadService;
	@Autowired
	private GoodsService goodsService;
	
	@GetMapping
	public Page<Product> findAll(@RequestParam("kw") Optional<String> kw,
			@RequestParam("currentPage") Optional<Integer> currentPage){
		return productService.findAllByKeyword(kw, currentPage);
	}
	@GetMapping("/all")
	public List<Product> findAll(){
		return productService.findAll();
	}
	
	@GetMapping("/{id}")
	public JsonNode findById(@PathVariable("id") Integer id) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		
		Product product = productService.findById(id);
		JsonNode productJSON = mapper.convertValue(product, JsonNode.class);
		node.put("product", productJSON);
		
		List<Goods> lstGoods = goodsService.findByProductId(id);
		JsonNode lstGoodsJSON = mapper.convertValue(lstGoods, JsonNode.class);
		node.put("lstGoods", lstGoodsJSON);
		return node;
	}
	@GetMapping("/last-id")
	public Integer findLastId() {
		List<Product> products = productService.findAll();
		int sizeOfProducts = products.size();
		return products.get(sizeOfProducts-1).getId();
	}
	
	@PostMapping
	public ResponseEntity<Product> create(@RequestBody Product product){
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
			if(productService.existInputDetail(id)) {
				product.setActive(false);
				productService.save(product);
				node.put("isFound", true);
				node.put("isExist", true);
				return node;
			}else {
				List<Goods> lstGoods = goodsService.findByProductId(id);
				if(lstGoods.size() > 0) {
					for(Goods g : lstGoods) {
						goodsService.deleteById(g.getId());
					}
				}
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
