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
import com.qlnt.model.PromotionDetail;
import com.qlnt.model.Brand;
import com.qlnt.model.Promotion;
import com.qlnt.service.PromotionDetailService;
import com.qlnt.service.PromotionService;
import com.qlnt.service.UploadService;

@RestController
@RequestMapping("/api/promotions")
public class PromotionAPI {
	@Autowired
	private PromotionService promoService;
	@Autowired
	private PromotionDetailService promoDetailService;
	@Autowired
	private UploadService uploadService;
	
	@GetMapping
	public Page<Promotion> findAll(@RequestParam("kw") Optional<String> kw,
			@RequestParam("currentPage") Optional<Integer> currentPage){
		return promoService.findAll(kw, currentPage);
	}
	@GetMapping("/{id}")
	public ResponseEntity<Promotion> getById(@PathVariable("id") String id) {
		if(promoService.existById(id)) {
			return ResponseEntity.ok(promoService.findById(id));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	@GetMapping("/promotion-detail/{id}")
	public ResponseEntity<List<PromotionDetail>> getDetailById(@PathVariable("id") String id){
		if(promoService.existById(id)) {
			return ResponseEntity.ok(promoDetailService.findByPromotionId(id));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	@GetMapping("/promotion-detail/detail/{id}")
	public ResponseEntity<PromotionDetail> getDetailById(@PathVariable("id") Integer id){
		if(promoDetailService.existById(id)) {
			return ResponseEntity.ok(promoDetailService.findById(id));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	@PostMapping
	public ResponseEntity<Promotion> create(@RequestBody Promotion promotion){
		if (promoService.existById(promotion.getId())) {
			return ResponseEntity.badRequest().build();
		}else {
			return ResponseEntity.ok(promoService.save(promotion));
		}
	}
	@PostMapping("/promotion-detail")
	public ResponseEntity<PromotionDetail> create(@RequestBody PromotionDetail promotionDetail){
		if (promoDetailService.existById(promotionDetail.getId())) {
			return ResponseEntity.badRequest().build();
		}else {
			return ResponseEntity.ok(promoDetailService.save(promotionDetail));
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Promotion> update(@PathVariable("id") String id,
			@RequestBody Promotion promotion){
		if (promoService.existById(id)) {
			String imgOld = promoService.findById(id).getPhoto();
			if(!promotion.getPhoto().equals(imgOld) && !imgOld.equals("photo.png")) {
				uploadService.delete("promotion", imgOld);
			}
			return ResponseEntity.ok(promoService.save(promotion));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	@PutMapping("/promotion-detail/{id}")
	public ResponseEntity<PromotionDetail> update(@PathVariable("id") Integer id,
			@RequestBody PromotionDetail promotionDetail){
		if (promoDetailService.existById(id)) {
			return ResponseEntity.ok(promoDetailService.save(promotionDetail));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") String id){
		if(promoService.existById(id)) {
			List<PromotionDetail> promotionDetails = promoDetailService.findByPromotionId(id);
			if(promotionDetails.size() > 0) {
				for(PromotionDetail p : promotionDetails) {
					promoDetailService.deleteById(p.getId());
				}
			}
			promoService.deleteById(id);
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	@DeleteMapping("/promotion-detail/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
		if (promoDetailService.existById(id)) {
			promoDetailService.deleteById(id);
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.notFound().build();
		}
	}
}
