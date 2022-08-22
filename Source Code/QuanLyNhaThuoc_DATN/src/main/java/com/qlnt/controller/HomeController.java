package com.qlnt.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.qlnt.model.Brand;
import com.qlnt.service.BrandService;
import com.qlnt.service.GoodsService;
import com.qlnt.service.PromotionService;

@Controller
public class HomeController {
	@Autowired
	private GoodsService goodsServie;
	@Autowired
	private BrandService brandService;
	@Autowired
	private PromotionService promoService;
	
	@GetMapping
	public String index(Model model) {
		List<Map<String, Object>> goodsOnSale = goodsServie.findGoodsForSaleOf();
		model.addAttribute("goodsOnSale", goodsOnSale);
		List<Map<String, Object>> goodsNewest = goodsServie.findGoodsForNewest();
		model.addAttribute("goodsNewest", goodsNewest);
		model.addAttribute("promotions", promoService.findPromotionAvailble());
		List<Map<String, Object>> bestSeller = goodsServie.findGoodsForBestSeller();
		model.addAttribute("bestSeller", bestSeller);
//		List<Brand> brands = brandService.findAll();
//		model.addAttribute("brands", brands);
		return "home/index";
	}
	@GetMapping("/brand")
	public String index(@RequestParam(defaultValue = "0") Integer page,
			Model model) {
		model.addAttribute("map", brandService.findBrandForListBrand(page));
		return "brand/list";
	}
	@GetMapping("/promotion")
	public String promotion(Model model) {
		model.addAttribute("promotions", promoService.findPromotionAvailble());
		return "promotion/list";
	}
}
