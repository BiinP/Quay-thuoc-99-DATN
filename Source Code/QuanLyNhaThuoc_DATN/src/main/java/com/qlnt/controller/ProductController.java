package com.qlnt.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.qlnt.model.Goods;
import com.qlnt.model.Product;
import com.qlnt.model.SubCategory;
import com.qlnt.service.GoodsService;
import com.qlnt.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {
	@Autowired
	private ProductService productService;
	@Autowired
	private GoodsService goodsService;

	@GetMapping("")
	public String index(@RequestParam(defaultValue = "0") Integer page, 
			@RequestParam(defaultValue = "") String brandId,
			@RequestParam(defaultValue = "") String subCateId, 
			@RequestParam(defaultValue = "0") Integer range,
			@RequestParam(defaultValue = "default") String sort,
			Model model) {
		List<Map<String, Object>> db = goodsService.findGoodsForListProduct(brandId, subCateId, range, page, sort);
		model.addAttribute("lstGoods", db);
		String[] brandIdAsArray;
		try {
			brandIdAsArray = brandId.split(",");
		} catch (Exception e) {
			brandIdAsArray = null;
		}
		model.addAttribute("brandId", brandId == null ? "" : brandId);
		model.addAttribute("brandIdAsList", brandIdAsArray == null ? "" : brandIdAsArray);
		model.addAttribute("subCateId", subCateId == null ? "" : subCateId);
		model.addAttribute("range", range == null ? "" : range);
		model.addAttribute("page", page);
		model.addAttribute("sort", sort);

		return "product/list";
	}

	@GetMapping("/{id}")
	public String detail(@PathVariable("id") Integer id, Model model, Principal principal) {
		Map<String, Object> goods = goodsService.findDetailGoods(id, principal);
		model.addAttribute("goods", goods);

		SubCategory subCate = (SubCategory) goods.get("subCategory");
		String subCateId = subCate.getId();
		List<Map<String, Object>> goodsRelated = goodsService.findRelatedGoodsBySubCate(subCateId);
		model.addAttribute("goodsRelated", goodsRelated);
		return "product/detail";
	}
	@GetMapping("/search")
	public String search(@RequestParam(defaultValue = "") String kw, 
			@RequestParam(defaultValue = "0") Integer page,
			Model model) {
		if(kw.equals("")) {
			return "redirect:/product";
		}
		else {
			List<Map<String, Object>> db = goodsService.findGoodsForSearch(kw, page);
			model.addAttribute("page", page);
			model.addAttribute("kw", kw);
			model.addAttribute("lstGoods", db);
			return "product/search";
		}
	}
}
